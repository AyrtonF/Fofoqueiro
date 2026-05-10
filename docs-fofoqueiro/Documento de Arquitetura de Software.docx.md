# **Documento de Arquitetura de Software (DAS)**

**Projeto:** Fofoqueiro (Plataforma VMS/VSaaS)

**Instituição:** Desenvolvimento Interno \- MVP

**Status:** EM ELABORAÇÃO

**Data:** 24 de Maio de 2024

## **Controle de Documento**

### **Histórico de Versões**

| Versão | Data | Autor | Descrição da Mudança |
| :---- | :---- | :---- | :---- |
| 1.0 | 24/05/2024 | Gemini | Definição da arquitetura base para o MVP (WebRTC \+ S3 \+ Clean Arch). |

## **1\. Introdução**

### **1.1 Propósito e Escopo do Sistema**

O **Fofoqueiro** é um sistema de gerenciamento de vídeo (VMS) moderno, operando no modelo *Video Surveillance as a Service* (VSaaS). O sistema visa democratizar o acesso ao monitoramento inteligente, resolvendo problemas críticos de latência de transmissão e altos custos de armazenamento em nuvem. O escopo do MVP abrange desde a ingestão de fluxos RTSP de câmeras IP até a visualização em tempo real via WebRTC e gravação em armazenamento S3 com custo de saída (egress) reduzido.

### **1.2 Audiência**

Este documento destina-se à equipe de engenharia de software, DevOps, analistas de segurança e stakeholders interessados na viabilidade técnica e financeira da plataforma.

### **1.3 Definições, Acrônimos e Abreviações**

#### **I. Conceitos de Negócio e Operacionais**

* **VMS (Video Management System):** Software para gerenciar e gravar fluxos de vídeo.  
* **VSaaS (Video Surveillance as a Service):** Modelo de negócio onde o monitoramento é oferecido como serviço em nuvem.  
* **Mosaico Dinâmico:** Interface que agrupa múltiplas câmeras em uma única tela.  
* **Vizinhança Colaborativa:** Recurso social para compartilhamento de alertas entre usuários de um mesmo grupo.

#### **II. Termos de Arquitetura de Software**

* **Clean Architecture:** Organização em camadas para isolar regras de negócio de frameworks.  
* **Transmuxing:** Processo de mudar o encapsulamento do vídeo (ex: RTSP para WebRTC) sem re-codificar, economizando CPU.  
* **Resolution Ladder:** Uso de sub-streams (baixa res) para mosaicos e main-streams (alta res) para tela cheia.

#### **III. Termos de Infraestrutura e Plataforma**

* **WebRTC:** Protocolo para comunicação em tempo real com latência \< 500ms.  
* **HLS (HTTP Live Streaming):** Protocolo baseado em segmentos para reprodução de gravações.  
* **Edge Computing:** Processamento realizado próximo à origem dos dados (câmeras).  
* **Egress Fees:** Taxas cobradas por provedores de nuvem pela saída de dados.

#### **IV. Termos de Persistência e Armazenamento**

* **S3 (Simple Storage Service):** Protocolo padrão para armazenamento de objetos.  
* **Cloudflare R2 / Wasabi:** Provedores S3 focados em custo zero de egress.  
* **JSONB:** Armazenamento de dados semi-estruturados no PostgreSQL para auditoria.

#### **V. Termos de Segurança da Informação**

* **RBAC (Role-Based Access Control):** Controle de acesso baseado em funções.  
* **MFA (Multi-Factor Authentication):** Segundo fator de autenticação para acesso às imagens.  
* **Mascaramento de Privacidade:** Zonas opacas no vídeo para proteger a privacidade de terceiros (LGPD).

## **2\. Metas e Restrições Arquiteturais**

### **2.1 Metas de Design**

* **Latência Ultrabaixa:** Visualização ao vivo com atraso inferior a 500ms para controle PTZ.  
* **Eficiência de Banda:** Otimização de tráfego via sub-streams e transmuxing eficiente.  
* **Sustentabilidade Financeira:** Arquitetura focada em minimizar custos de tráfego de saída de vídeo.  
* **Multi-Tenancy:** Isolamento completo de dados entre diferentes clientes/condomínios.

### **2.2 Restrições Técnicas**

* **Backend:** Java Spring Boot 3 ou Node.js (focado em I/O assíncrono).  
* **Frontend:** Next.js (React) com suporte a Web Workers para decodificação de vídeo.  
* **Banco de Dados:** PostgreSQL 16 com RLS (Row Level Security).  
* **Armazenamento:** Compatível com protocolo S3 (prioridade para Cloudflare R2).

### **2.3 Princípios Arquiteturais**

* **Privacy by Design:** Zonas de mascaramento e criptografia como requisitos nativos.  
* **Auditability:** Rastreabilidade de quem visualizou cada segundo de vídeo.  
* **Separation of Concerns:** Desacoplamento entre o Gateway de Streaming e a API de Gestão.

## **3\. Representação Arquitetural**

### **3.1 Estilo Arquitetural: Sistema Distribuído**

O sistema é composto por microserviços/módulos desacoplados:

1. **API Gateway / Auth:** Gestão de usuários e permissões.  
2. **Streaming Gateway:** Responsável pelo Transmuxing (RTSP \-\> WebRTC/HLS).  
3. **Storage Handler:** Gerencia o ciclo de vida das gravações no S3.

### **3.4 Backend: Clean Architecture**

Organizado para proteger a lógica de negócio de monitoramento (alertas, gestão de dispositivos) das tecnologias de streaming.

### **3.5 Frontend: Next.js**

Interface responsiva com suporte a players customizados (WebRTC) e renderização de mosaicos otimizada via GPU.

## **4\. Visão Lógica**

### **4.1 Decomposição em Camadas**

* **Apresentação:** Controllers REST e sinalização WebRTC.  
* **Aplicação (Use Cases):** Orquestração de visualização, busca de gravações e gestão de grupos.  
* **Domínio:** Entidades como Câmera, Tenant, Gravação e Alerta.  
* **Infraestrutura:** Adapters para S3, Driver de Câmeras e Persistência Postgres.

## **6\. Visão de Implantação**

### **6.2 Especificação dos Recursos (Nós de Rede)**

| Componente | Função | Tecnologia | Portas | Requisito Estimado |
| :---- | :---- | :---- | :---- | :---- |
| **VM 01** | Borda / Proxy | Nginx / SSL | 80/443 | 2 vCPU, 2GB RAM |
| **VM 02** | Frontend | Next.js | 3000 | 2 vCPU, 4GB RAM |
| **VM 03** | API / Backend | Spring Boot | 8080 | 4 vCPU, 8GB RAM |
| **VM 04** | Streaming Gateway | Media MTX / Go2RTC | 8554/554 | 4 vCPU, 8GB RAM (Alta Rede) |
| **Cloud S3** | Custódia de Vídeo | Cloudflare R2 | 443 | Armazenamento Ilimitado |

## **7\. Visão de Dados**

### **7.1 Modelo de Persistência**

* **Relacional (PostgreSQL):** Dados de configuração, usuários, logs de auditoria e metadados de câmeras.  
* **Objetos (S3):** Chunks de vídeo (.ts) e snapshots (.jpg).

## **8\. Requisitos Não Funcionais**

### **8.1 Segurança da Informação**

#### **Autenticação**

* JWT com renovação via Refresh Token.  
* MFA obrigatório para perfis Administrativos.

#### **Autorização**

| Perfil | Responsabilidade |
| :---- | :---- |
| **Super Admin** | Gestão de Tenants (Revendas) e White-label. |
| **Admin Tenant** | Gestão de câmeras, usuários do condomínio e relatórios. |
| **Operador** | Visualização ao vivo e busca de gravações. |
| **Usuário Final** | Acesso limitado às câmeras permitidas em sua conta. |

### **8.2 Escalabilidade e Performance**

#### **Metas de Performance**

| Operação | Tempo Máximo |
| :---- | :---- |
| Inicialização de Vídeo (Live) | \< 1.5 s |
| Latência WebRTC | \< 500 ms |
| Busca em Timeline (Playback) | \< 2.0 s |

## **11\. Registro de Decisões de Arquitetura (ADR)**

### **ADR-001: Uso de WebRTC para Monitoramento ao Vivo**

**Contexto:** Operadores precisam de resposta imediata ao mover câmeras PTZ.

**Decisão:** Adotar WebRTC como protocolo principal de live.

**Consequência:** Baixa latência, mas exige servidores de sinalização e STUN/TURN.

### **ADR-002: Armazenamento S3 com Custo Zero de Egress**

**Contexto:** Visualização de vídeo gera alto tráfego de saída, inviabilizando custos na AWS/Azure.

**Decisão:** Utilizar Cloudflare R2 para armazenamento de gravações.

**Consequência:** Previsibilidade financeira e escalabilidade global.

### **ADR-003: Estratégia de Resolution Ladder**

**Contexto:** Mosaicos com muitas câmeras travam o navegador do cliente.

**Decisão:** Forçar uso de sub-stream (resolução menor) em mosaicos.

**Consequência:** Economia de banda de até 70% na visualização simultânea.
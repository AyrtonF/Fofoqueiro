# **Backlog de Produto: Plataforma "Fofoqueiro" (MVP)**

A plataforma **Fofoqueiro** é um VMS (Video Management System) moderno focado em democratizar o acesso ao monitoramento inteligente, garantindo baixa latência, custos de infraestrutura sustentáveis e conformidade rigorosa com a LGPD.

## **1\. Visão do Produto**

**Para** empresas de segurança e condomínios que precisam de monitoramento ágil, **o Fofoqueiro** é uma plataforma de VSaaS (Video Surveillance as a Service) **que** permite visualização em tempo real e colaborativa, **diferente** de sistemas analógicos lentos ou soluções de nuvem com custos proibitivos de saída de dados (egress).

## **2\. Backlog Detalhado: Épicos e Funcionalidades**

### **Épico 1: Ingestão e Visualização (O Coração do Produto)**

| Funcionalidade | Necessidade que Resolve | Requisito Técnico Chave |
| :---- | :---- | :---- |
| **Gateway de Transmuxing (RTSP to Web)** | Permite ver câmeras IP comuns diretamente no navegador sem plugins. | Conversão de RTSP/RTMP para WebRTC (Live) e HLS (Playback). |
| **Mosaico Dinâmico (Adaptive Grid)** | Evita o travamento do computador do usuário ao abrir muitas câmeras. | Uso de *Resolution Ladder* (sub-streams para miniaturas e main-stream para tela cheia). |
| **Player de Baixa Latência (WebRTC)** | Essencial para operadores que precisam controlar câmeras PTZ (movimento) em tempo real. | Latência inferior a 500ms para garantir precisão no controle. |

### **Épico 2: Gestão e Operação (Multi-Tenancy)**

| Funcionalidade | Necessidade que Resolve | Requisito Técnico Chave |
| :---- | :---- | :---- |
| **Arquitetura Multi-Tenant** | Permite que uma única instalação atenda vários clientes (condomínios/empresas) isoladamente. | Isolamento lógico via tenant\_id e Row-Level Security (RLS) no banco de dados. |
| **Painel White-Label** | Revendedores podem aplicar sua própria marca, aumentando o valor percebido do serviço. | Carregamento dinâmico de CSS/Logos baseado no domínio de acesso. |
| **Mapa Interativo (Leaflet.js)** | Localização visual rápida de câmeras em grandes plantas ou cidades. | Integração com OpenStreetMap e marcação de status (online/offline) no ícone. |

### **Épico 3: Inteligência e Saúde (Proatividade)**

| Funcionalidade | Necessidade que Resolve | Requisito Técnico Chave |
| :---- | :---- | :---- |
| **Monitoramento de Saúde (CHMS)** | Evita descobrir que uma câmera não gravou apenas após o crime ocorrer. | Alertas de *Heartbeat* e monitoramento de *Recording Confidence*. |
| **Detecção de Movimento e Alertas** | Filtra o que é importante, evitando que o usuário tenha que olhar para a tela 24/7. | Metadados de analíticos vindos da câmera ou processados via gateway. |
| **Linha do Tempo de Gravações** | Busca rápida por eventos passados para coleta de provas. | Interface de scroll fluido integrada com armazenamento S3 de custo zero (Cloudflare R2). |

### **Épico 4: Segurança, Auditoria e LGPD**

| Funcionalidade | Necessidade que Resolve | Requisito Técnico Chave |
| :---- | :---- | :---- |
| **Zonas de Mascaramento de Privacidade** | Garante que o vizinho não seja filmado dentro de casa, cumprindo a LGPD. | Overlay de máscara estática sobre o stream de vídeo. |
| **Logs de Auditoria Imutáveis** | Segurança jurídica para saber exatamente quem visualizou ou baixou quais imagens. | Armazenamento de logs (Quem, O quê, Quando, Onde) em serviço isolado. |
| **Criptografia de Ponta a Ponta** | Protege as imagens contra interceptação e acessos não autorizados. | Uso obrigatório de TLS/HTTPS e autenticação de dois fatores (2FA). |

## **3\. Priorização (Método MoSCoW)**

### **Must-Have (Obrigatório para o Lançamento)**

1. Ingestão RTSP com entrega via WebRTC/HLS.  
2. Login seguro e isolamento de dados por Tenant (Cliente).  
3. Visualização em Mosaico (até 16 telas) com otimização de banda.  
4. Player de Playback para buscar gravações.  
5. Cadastro de câmeras com os 10 campos essenciais (ID, Gateway, Retenção, etc.).  
6. Mascaramento de privacidade (Conformidade LGPD básica).

### **Should-Have (Importante para o Próximo Ciclo)**

1. Mapa interativo com geolocalização das câmeras.  
2. Alertas de câmera offline via Push/E-mail.  
3. Relatórios semanais de saúde (Uptime das câmeras).  
4. Suporte a White-Label (Customização de cores e logos).

### **Could-Have (Diferenciais de Mercado)**

1. **Chat de Vizinhança Colaborativa:** Onde usuários do mesmo grupo podem trocar mensagens e compartilhar alertas.  
2. Busca inteligente por objetos (Pessoas, Veículos) usando IA na nuvem.  
3. Aplicativo mobile nativo para visualização rápida.

## **4\. Considerações de Infraestrutura para o Produto**

Para que o "Fofoqueiro" seja viável financeiramente, a estratégia de produto deve seguir:

* **Zero Egress Fees:** Utilizar **Cloudflare R2** ou **Wasabi** para o armazenamento de vídeo, garantindo que a visualização intensa pelos usuários não gere faturas astronômicas de tráfego de saída.  
* **Edge Computing:** Processar o transmuxing o mais perto possível da câmera para reduzir a latência percebida pelo usuário final.  
* **Escada de Resolução:** Forçar o uso de sub-streams no mosaico como padrão de fábrica para economizar largura de banda do cliente e processamento do servidor.
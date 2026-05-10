# **Backlog de Design: Visão de Solução e Componentes \- Plataforma "Fofoqueiro"**

Este documento detalha a experiência do usuário (UX) e os componentes de interface (UI) necessários para o MVP da plataforma, conectando as necessidades de negócio às tarefas técnicas.

## **1\. Módulo: Autenticação e Acesso (Segurança & White-Label)**

**Necessidade:** Login seguro, isolamento de dados e personalização de marca.

| Funcionalidade | Solução de Design (Visão) | Elementos de Interface | Regras de Interação / UX |
| :---- | :---- | :---- | :---- |
| **Portal de Login White-Label** | Página de entrada que identifica o tenant pelo domínio ou subdomínio. | Logo do parceiro, campos de e-mail/senha, botão de login, link "Esqueci minha senha". | O sistema busca o tenant\_id via domínio. Se houver configuração, troca cores primárias e logo. |
| **Autenticação em Duas Etapas (2FA)** | Modal ou página secundária após o login bem-sucedido para validação extra. | Campo de 6 dígitos, cronômetro de expiração, botão "Reenviar código". | Disparo automático de e-mail/SMS. Bloqueio após 3 tentativas erradas. |

## **2\. Módulo: Monitoramento ao Vivo (O Mosaico Dinâmico)**

**Necessidade:** Visualização fluida de múltiplas câmeras sem travar o dispositivo do usuário.

| Funcionalidade | Solução de Design (Visão) | Elementos de Interface | Regras de Interação / UX |
| :---- | :---- | :---- | :---- |
| **Grid de Visualização (Mosaico)** | Área central com layouts pré-definidos (1x1, 2x2, 3x3, 4x4). | Slot de vídeo, nome da câmera, ícone de status (Rec/Live), botão de expansão. | **Resolution Ladder:** Câmeras no grid carregam Sub-stream (LQ). Ao clicar em "Expandir", troca para Main-stream (HQ) WebRTC. |
| **Barra Lateral de Dispositivos** | Lista hierárquica (Árvore) de câmeras organizadas por grupos/locais. | Campo de busca, lista com ícones de estado (online/offline), badges de alertas ativos. | Drag-and-drop: Usuário arrasta a câmera da lista para um slot vazio do mosaico para iniciar o stream. |
| **Controles de Player WebRTC** | Overlay sobre o vídeo para interações imediatas. | Botão de Screenshot, Mute, Gravação Manual, Controle PTZ (setas direcionais se disponível). | Hover: Os controles aparecem apenas quando o mouse está sobre o vídeo para manter a interface limpa. |

## **3\. Módulo: Gestão de Dispositivos e Mapas**

**Necessidade:** Cadastro rápido e localização espacial das câmeras.

| Funcionalidade | Solução de Design (Visão) | Elementos de Interface | Regras de Interação / UX |
| :---- | :---- | :---- | :---- |
| **Mapa Interativo (Leaflet)** | Mapa em tela cheia com marcadores geográficos das câmeras. | Pins coloridos (Verde=Online, Vermelho=Offline), Cluster de pins (para câmeras próximas). | Clique no Pin: Abre um "Popup" com o Live Stream minificado daquela câmera específica. |
| **Formulário de Cadastro (Wizard)** | Processo passo-a-passo para adicionar câmeras com validação técnica. | Campos: Nome, URL RTSP, Gateway associado, tempo de retenção (dias), coordenadas GPS. | Validação em tempo real: Botão "Testar Conexão" que tenta abrir o stream antes de salvar. |

## **4\. Módulo: Inteligência e Playback (Timeline)**

**Necessidade:** Buscar evidências de forma rápida em gravações passadas.

| Funcionalidade | Solução de Design (Visão) | Elementos de Interface | Regras de Interação / UX |
| :---- | :---- | :---- | :---- |
| **Timeline de Eventos** | Régua de tempo horizontal abaixo do vídeo com marcações de eventos. | Cursor de tempo (seeker), faixas de cor (Azul=Gravado, Amarelo=Movimento), seletor de data. | Scroll do mouse: Faz zoom na timeline (minutos \-\> segundos). Arrastar o seeker atualiza o player (HLS). |
| **Painel de Alertas de IA** | Lista lateral ou inferior com miniaturas de detecções (Pessoas/Carros). | Thumbnail do evento, horário, tipo de objeto, link "Ver na Timeline". | Clicar no alerta: O player de vídeo salta exatamente para 5 segundos antes do evento ocorrer. |

## **5\. Módulo: Administração e Auditoria (LGPD)**

**Necessidade:** Conformidade legal e controle de quem viu o quê.

| Funcionalidade | Solução de Design (Visão) | Elementos de Interface | Regras de Interação / UX |
| :---- | :---- | :---- | :---- |
| **Editor de Máscara de Privacidade** | Ferramenta de desenho sobre um frame parado da câmera. | Canvas sobre o vídeo, ferramenta de retângulo, botão "Aplicar Máscara". | O usuário desenha áreas pretas que são "queimadas" no stream/gravação para proteger vizinhos. |
| **Log de Auditoria Forense** | Tabela densa com filtros avançados de busca. | Filtros (Data, Usuário, Câmera, Ação), Tabela com ID da Sessão e IP de origem. | Exportação: Botão para gerar PDF/CSV assinado digitalmente com o Hash de integridade. |

## **Detalhamento de Dados para Desenvolvimento (Endpoints Estimados)**

Para que o time comece a trabalhar, os seguintes contratos de dados são prioritários:

1. **GET /api/v1/tenant/config:** Retorna logos, cores primárias e nome da marca baseado no domínio.  
2. **GET /api/v1/cameras/stream/{id}:** Retorna o sinalizador WebRTC (SDP/ICE Servers) para a câmera X.  
3. **GET /api/v1/cameras/health:** Retorna status em tempo real (Online/Offline, FPS atual, Bitrate).  
4. **GET /api/v1/playback/{id}/events:** Retorna lista de intervalos de tempo que possuem gravação no S3.  
5. **POST /api/v1/audit/log:** Endpoint interno que registra cada clique de "Visualizar" ou "Download".

## **Guia de Estilo (UI Kit)**

* **Cores Padrão:** Dark Mode (Fundo \#0F172A), Primária (Azul \#3B82F6 \- alterável por White-Label).  
* **Tipografia:** Sans-serif (Inter ou Roboto) para leitura técnica rápida.  
* **Componentes:** Uso de Tailwind CSS para layouts responsivos (Mobile-first para alertas, Desktop-first para Mosaicos).
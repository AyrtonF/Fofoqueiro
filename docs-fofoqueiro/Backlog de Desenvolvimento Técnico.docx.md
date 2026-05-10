# **Backlog de Desenvolvimento Técnico: Projeto Fofoqueiro**

Este backlog detalha as tarefas necessárias para a construção do MVP, divididas por especialidades técnicas e módulos funcionais.

## **Módulo 0: Fundação, Infraestrutura e Segurança (Base)**

*O objetivo aqui é preparar o terreno para o isolamento de dados e comunicação segura.*

* \[Banco de Dados\] Criar schema inicial com suporte a Multi-tenancy (Tabelas: tenants, users, roles).  
* \[Banco de Dados\] Implementar Row-Level Security (RLS) no PostgreSQL para garantir que um tenant nunca acesse dados de outro.  
* \[Backend\] Configurar projeto Spring Boot 3 com Clean Architecture (Camadas: Domain, Application, Infrastructure).  
* \[Backend\] Implementar autenticação via JWT com Refresh Token e lógica de identificação de tenant via domínio.  
* \[Backend\] Desenvolver serviço de envio de OTP (MFA) via e-mail institucional.  
* \[DevOps\] Configurar VM de Borda com Nginx como Proxy Reverso e certificado SSL (Certbot).  
* \[DevOps\] Provisionar Bucket no Cloudflare R2 (ou Wasabi) e configurar credenciais de acesso via API S3.  
* \[Design\] Definir Style Guide (Cores primárias, tipografia Inter, componentes básicos do sistema).

## **Módulo 1: Ingestão e Gateway de Streaming**

*O "coração" da plataforma: transformar RTSP em algo que o navegador entenda.*

* \[DevOps\] Instalar e configurar Streaming Gateway (MediaMTX ou Go2RTC) em VM dedicada.  
* \[Backend\] Desenvolver lógica de sinalização WebRTC (troca de SDP e ICE Servers) entre o cliente e o gateway.  
* \[Backend\] Criar Worker para monitoramento de Heartbeat (Saúde) das câmeras via ICMP/RTSP.  
* \[Frontend\] Criar componente WebRTCPlayer robusto com lógica de auto-reconnect e buffer adaptativo.  
* \[Teste\] Validar latência de transmissão em redes 4G/5G (Meta: \< 500ms).

## **Módulo 2: Gerenciamento de Dispositivos e Mapas**

*Organização e visualização espacial das câmeras.*

* \[Banco de Dados\] Modelar tabelas cameras, gateways e group\_locations.  
* \[Backend\] Desenvolver endpoints de CRUD para câmeras (Incluindo validação de URL RTSP).  
* \[Backend\] Criar endpoint para retornar configurações de White-label (cores/logos) baseadas no tenant\_id.  
* \[Frontend\] Desenvolver página de listagem de câmeras com filtros por status e grupo.  
* \[Frontend\] Integrar Leaflet.js para exibição de mapa com pins dinâmicos (Verde/Vermelho).  
* \[Ux\] Criar fluxo de "Wizard" para cadastro de câmeras para evitar erros de configuração do usuário.  
* \[Design\] Desenvolver UI dos marcadores de mapa e popups de visualização rápida.

## **Módulo 3: Monitoramento ao Vivo (O Mosaico)**

*A interface principal do operador.*

* \[Frontend\] Desenvolver Grid de Mosaico (CSS Grid) com suporte a layouts 1x1, 2x2, 3x3 e 4x4.  
* \[Frontend\] Implementar lógica de "Resolution Ladder": solicitar sub-stream no mosaico e main-stream no modo tela cheia.  
* \[Frontend\] Criar Sidebar de dispositivos com árvore hierárquica e busca em tempo real.  
* \[Ux\] Implementar funcionalidade de Drag-and-drop para arrastar câmeras da lista para os slots do mosaico.  
* \[Frontend\] Desenvolver Overlay de controles do player (Screenshot, Mute, Gravação Manual).  
* \[Teste\] Testar consumo de CPU/GPU do navegador com 16 câmeras simultâneas no mosaico.

## **Módulo 4: Playback e Timeline de Gravações**

*Recuperação de evidências passadas.*

* \[Backend\] Desenvolver "Storage Handler" para fatiar fluxos em segmentos .ts e salvar no Cloudflare R2.  
* \[Backend\] Criar endpoint para listagem de eventos de gravação filtrados por data e câmera.  
* \[Frontend\] Desenvolver componente de Timeline (Régua de tempo) com zoom e scroll.  
* \[Frontend\] Implementar player HLS para reprodução das gravações armazenadas no S3.  
* \[Teste\] Verificar integridade das gravações após oscilações de conexão do Gateway.

## **Módulo 5: Auditoria, LGPD e Administração**

*Conformidade legal e segurança jurídica.*

* \[Banco de Dados\] Criar tabela audit\_logs com campo row\_hash para integridade SHA-256.  
* \[Backend\] Implementar interceptor global para logar todas as visualizações de vídeo (Quem, Onde, Quando).  
* \[Backend\] Desenvolver lógica de Mascaramento de Privacidade (desenho de zonas opacas via Canvas/SVG).  
* \[Frontend\] Criar módulo de administração para gestão de usuários do Tenant (RBAC).  
* \[Frontend\] Desenvolver página de relatório de auditoria forense com exportação para PDF.  
* \[Ux\] Verificar se os termos de uso e política de privacidade estão visíveis e em conformidade com a LGPD.

## **Resumo de Esforço Estimado (Sprint 1-4)**

1. **Sprint 1:** Fundação \+ Auth \+ Ingestão Básica (WebRTC).  
2. **Sprint 2:** CRUD de Câmeras \+ Mapa \+ Layout do Mosaico.  
3. **Sprint 3:** Gravação em S3 \+ Timeline \+ Playback HLS.  
4. **Sprint 4:** Auditoria \+ Mascaramento \+ Refinamento UX/UI.
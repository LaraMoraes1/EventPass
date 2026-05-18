# EventPass

Projeto feito para a faculdade com o objetivo de controlar a entrada e saída de participantes em eventos usando QR Code.

A ideia é simples: o participante se cadastra no app, escolhe um evento, se inscreve e recebe um QR Code. O administrador usa outro celular para escanear esse QR e registrar entrada ou saída.

- Android Studio
- Java
- XML tradicional para as telas
- Material Design
- Retrofit para consumir a API
- ZXing para gerar e ler QR Code
- Spring Boot no backend
- PostgreSQL online no Supabase
- Render para hospedar a API

## Estrutura do projeto

- `android-app/`: aplicativo Android.
- `backend/`: API Spring Boot.
- `database/schema.sql`: script do banco PostgreSQL.

## O que o app faz

- Cadastro e login de usuários.
- Dois tipos de usuário: administrador e participante.
- Cadastro, edição e destaque de eventos.
- Inscrição de participantes em eventos.
- Geração de QR Code por inscrição.
- Scanner de QR Code pelo administrador.
- Registro de entrada e saída.
- Histórico de acessos.
- Dashboard administrativo.
- Controle de permanência para certificado.

## Como funciona o QR Code

O QR Code não é apenas do usuário. Ele pertence a uma inscrição.

Exemplo: se uma pessoa se inscrever em dois eventos, ela terá dois QR Codes diferentes. Isso ajuda o sistema a saber exatamente qual participante está entrando em qual evento.

## Como rodar o backend

Entre na pasta do backend:

```bash
cd backend
```

Configure as variáveis do banco:

```bash
DB_URL=jdbc:postgresql://host:5432/postgres
DB_USER=usuario
DB_PASSWORD=senha
```

Depois execute a API:

```bash
mvn spring-boot:run
```

A API local roda em:

```text
http://localhost:8080
```

No projeto publicado, a API está configurada para rodar online pelo Render.

## Como abrir o app Android

1. Abra a pasta `android-app/` no Android Studio.
2. Espere o Gradle sincronizar.
3. Verifique a URL da API em:

```text
android-app/app/src/main/java/com/eventpass/mobile/api/ApiClient.java
```

4. Conecte um celular Android com depuração USB ativada.
5. Clique em Run.

## Arquivos importantes no Android

- `ApiClient.java`: configura a URL da API.
- `EventPassApi.java`: rotas usadas pelo Retrofit.
- `QrCodeActivity.java`: tela que gera o QR Code.
- `ScannerActivity.java`: tela que escaneia o QR Code.
- `res/layout/`: telas feitas em XML.

## Arquivos importantes no backend

- `controller/`: recebe as requisições da API.
- `service/`: regras de negócio.
- `repository/`: acesso ao banco com JPA.
- `model/`: entidades do banco.
- `dto/`: objetos de entrada e saída da API.

## Usuário de teste

Administrador:

```text
admin@eventpass.com
123456
```

Também é possível criar participantes pelo próprio app.

## Fluxo de demonstração

1. Entrar como participante.
2. Escolher um evento.
3. Fazer inscrição.
4. Abrir o QR Code.
5. Entrar como administrador em outro celular.
6. Escanear o QR Code.
7. Conferir entrada, saída e histórico.

## Observação

O app foi feito para Android. O APK não instala em iPhone.

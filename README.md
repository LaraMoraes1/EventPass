# EventPass - Smart Event Access Mobile

Sistema completo para controle de acesso em palestras e eventos usando Android Java/XML, Spring Boot, PostgreSQL online e QR Code com ZXing.

## Estrutura

- `android-app/`: aplicativo Android tradicional em Java, XML e Material Design.
- `backend/`: API REST Spring Boot em Java.
- `database/schema.sql`: estrutura PostgreSQL com dados de exemplo.
- `docs/demo-checklist.md`: roteiro rápido para apresentação ao vivo.

## Como executar a API

1. Crie um banco PostgreSQL online em Neon, Supabase, Render, Railway ou outro serviço.
2. Execute o script `database/schema.sql`.
3. Configure as variáveis de ambiente:

```powershell
$env:DB_URL="jdbc:postgresql://HOST:5432/NOME_DO_BANCO"
$env:DB_USER="usuario"
$env:DB_PASSWORD="senha"
```

4. Inicie a API:

```powershell
cd backend
mvn spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Como abrir o app no Android Studio

1. Abra a pasta `android-app/` no Android Studio.
2. Sincronize o Gradle.
3. Em `ApiClient.java`, ajuste `BASE_URL`:
   - Emulador Android: `http://10.0.2.2:8080/api/`
   - Celular físico na mesma rede: `http://IP_DO_SEU_PC:8080/api/`
   - API publicada online: `https://sua-api.com/api/`
4. Rode no celular Android.

Checklist no Android Studio:

1. Use JDK 21 no backend Spring Boot.
2. Aguarde o download das dependências Gradle.
3. Conecte o celular com Depuração USB ativada.
4. Garanta que celular e notebook estejam na mesma rede Wi-Fi quando a API estiver rodando localmente.
5. Para celular físico, descubra o IP do notebook com `ipconfig` e use algo como `http://192.168.0.10:8080/api/`.
6. Se publicar a API online, use HTTPS no `BASE_URL`.

Arquivos importantes do app:

- `android-app/app/src/main/java/com/eventpass/mobile/api/ApiClient.java`: URL da API.
- `android-app/app/src/main/java/com/eventpass/mobile/api/EventPassApi.java`: endpoints Retrofit.
- `android-app/app/src/main/java/com/eventpass/mobile/ui/QrCodeActivity.java`: geração do QR Code com ZXing.
- `android-app/app/src/main/java/com/eventpass/mobile/ui/ScannerActivity.java`: leitura pela câmera com ZXing.
- `android-app/app/src/main/res/layout/`: telas XML tradicionais.

## Usuários de teste

- Admin: `admin@eventpass.com` / `123456`
- Participante: `ana@eventpass.com` / `123456`

## Fluxo principal da demonstração

1. Login como participante.
2. Entrar em um evento.
3. Abrir QR Code pessoal.
4. Login como administrador em outro celular.
5. Abrir Scanner.
6. Escanear QR: primeira leitura registra entrada; segunda leitura registra saída.
7. Ver Dashboard e Histórico atualizados.

## Bibliotecas principais

- Android: Material Components, Retrofit, Gson, ZXing Android Embedded.
- Backend: Spring Boot Web, Spring Data JPA, PostgreSQL Driver, Validation.

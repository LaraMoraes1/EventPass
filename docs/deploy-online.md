# Publicar EventPass Online

Objetivo para a apresentação: levar apenas o celular com o APK instalado.

Arquitetura final:

```text
App Android instalado no celular
↓
API Spring Boot publicada no Render
↓
PostgreSQL online no Supabase
```

## 1. Criar o banco no Supabase

1. Acesse `https://supabase.com`.
2. Crie um projeto chamado `eventpass`.
3. Guarde a senha do banco.
4. Abra `SQL Editor`.
5. Cole e execute o conteúdo de `database/schema.sql`.

## 2. Pegar a string de conexão correta

No Supabase, abra `Connect`.

Para usar no Render, prefira a opção `Session pooler`, porque ela funciona melhor em ambientes IPv4.

Você verá algo parecido com:

```text
postgresql://postgres.PROJECT_REF:SENHA@aws-0-us-east-1.pooler.supabase.com:5432/postgres
```

No Spring Boot, use assim:

```text
DB_URL=jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres
DB_USER=postgres.PROJECT_REF
DB_PASSWORD=SENHA
```

Se sua senha tiver caracteres como `@`, `#`, `%`, `:` ou `/`, crie uma senha mais simples no Supabase para evitar erro de conexão.

## 3. Subir o código para o GitHub

1. Crie um repositório no GitHub.
2. Envie este projeto para lá.
3. Confirme que a pasta `backend` está no repositório.

## 4. Publicar a API no Render

1. Acesse `https://render.com`.
2. Crie uma conta ou faça login.
3. Clique em `New +`.
4. Escolha `Web Service`.
5. Conecte seu repositório do GitHub.
6. Em `Root Directory`, coloque:

```text
backend
```

7. Em `Runtime`, escolha `Docker`.
8. Em `Environment Variables`, adicione:

```text
DB_URL=jdbc:postgresql://HOST_DO_SUPABASE:5432/postgres
DB_USER=postgres.PROJECT_REF
DB_PASSWORD=SUA_SENHA
```

9. Clique em `Create Web Service`.
10. Aguarde o deploy terminar.

## 5. Testar a API online

Quando o Render gerar a URL, teste:

```text
https://NOME-DO-SERVICO.onrender.com/api/health
```

Depois teste:

```text
https://NOME-DO-SERVICO.onrender.com/api/events
```

Se aparecer JSON, a API está funcionando.

## 6. Apontar o Android para a API online

Abra:

```text
android-app/app/src/main/java/com/eventpass/mobile/api/ApiClient.java
```

Troque:

```java
public static final String BASE_URL = "http://10.0.2.2:8080/api/";
```

por:

```java
public static final String BASE_URL = "https://NOME-DO-SERVICO.onrender.com/api/";
```

## 7. Gerar APK

No Android Studio:

1. Abra a pasta `android-app`.
2. Clique em `Build`.
3. Clique em `Build Bundle(s) / APK(s)`.
4. Clique em `Build APK(s)`.
5. Instale o APK no celular.

## 8. Teste final sem PC

No celular, usando internet móvel ou Wi-Fi:

1. Abra o app.
2. Faça login como participante.
3. Inscreva-se em um evento.
4. Abra o QR Code.
5. Faça login como admin.
6. Escaneie o QR.
7. Veja entrada e saída sendo registradas.

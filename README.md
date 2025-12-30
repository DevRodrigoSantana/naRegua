# ✂️ NaRegua API

API REST desenvolvida em **Spring Boot** para o sistema **NaRegua**, focado em **agendamentos de barbearias**.  
No momento, a aplicação contempla **cadastro e autenticação de usuários**, com segurança baseada em **JWT**.

---

## 🚀 Tecnologias Utilizadas

- Java 17  
- Spring Boot 3.5.x  
- Spring Security  
- Spring Data JPA  
- MySQL  
- JWT (JSON Web Token)  
- Swagger / OpenAPI (SpringDoc)  
- Maven  

---

## 🔐 Segurança

A API utiliza **JWT** para autenticação e autorização.

- Autenticação via `/api/v1/auth`
- Rotas protegidas exigem token Bearer
- Swagger liberado sem autenticação

---

## 📌 Funcionalidades Atuais

- Cadastro de usuários
- Autenticação com JWT
- Controle de acesso via Spring Security
- Documentação automática com Swagger

---

## 📄 Documentação da API (Swagger)

Após iniciar a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

yaml
Copiar código

---

## ⚙️ Configurações Principais

As variáveis sensíveis devem ser configuradas via **environment variables**:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`

---

## 🧩 Status do Projeto

🚧 Em desenvolvimento  
Próximas etapas incluem:
- Agendamentos
- Serviços da barbearia
- Gestão de horários e profissionais

---

## 👨‍💻 Autor

**Rodrigo Santana**  
📧 rssantos.dev@gmail.com
# 🎬 Screenmatch 2.0

Aplicação **Full Stack** para gerenciamento e visualização de séries, temporadas e episódios, construída com **Spring Boot (backend)** e **JavaScript (frontend)**.

O projeto oferece uma API REST robusta com regras de negócio reais e um frontend que consome os dados dinamicamente.

---

## 🚀 Sobre o Projeto

O **Screenmatch 2.0** é uma aplicação que permite explorar séries, visualizar episódios e aplicar filtros inteligentes como:

* séries mais bem avaliadas
* lançamentos recentes
* episódios por temporada
* top episódios por série
* filtragem por categoria

O sistema foi desenvolvido com foco em **boas práticas de arquitetura backend** e **integração full stack**.

---

## 🧠 Funcionalidades

### 🔙 Backend (API REST)

* 📺 Listagem de todas as séries
* 🔝 Top 5 séries por avaliação
* 🆕 Séries com episódios mais recentes
* 🔎 Busca de série por ID
* 🎞️ Listagem de episódios de uma série
* 📅 Filtro de episódios por temporada
* 🏆 Top 5 episódios por série
* 🎭 Filtro de séries por categoria

---

### 🌐 Frontend

* Consumo dinâmico da API com **fetch/Promises**
* Renderização de listas de séries no DOM
* Filtro por categoria integrado ao backend
* Navegação para página de detalhes
* Requisições paralelas com `Promise.all`

---

## 🔗 Endpoints da API

### 📌 Séries

```http
GET /series
GET /series/top5
GET /series/lancamentos
GET /series/{id}
GET /series/categoria/{genero}
```

### 📌 Episódios

```http
GET /series/{id}/temporadas/todas
GET /series/{id}/temporadas/{numero}
GET /series/{id}/temporadas/top
```

---

## 🛠️ Tecnologias Utilizadas

### 🔙 Backend

* Java 17+
* Spring Boot
* PostgreSQL
* Spring Data JPA
* Hibernate
* Maven

### 🌐 Frontend

* HTML5
* CSS3
* JavaScript (ES6+)

---

## 🧱 Arquitetura

O backend segue o padrão em camadas:

* **Controller** → expõe os endpoints REST
* **Service** → centraliza regras de negócio
* **Repository** → acesso ao banco com JPA
* **DTOs** → transferência de dados desacoplada
* **Model/Entity** → representação das entidades

---

## 💡 Destaques Técnicos

* Uso de **DTOs** para desacoplamento da API
* Queries avançadas com **Spring Data JPA** (`@Query`, JOINs, filtros e ordenações)
* Uso de **Streams e Optional** no Java
* Separação clara de responsabilidades (camadas)
* Integração real entre frontend e backend
* Requisições paralelas no frontend com `Promise.all`

---

## ⚙️ Como Executar o Projeto

### 🔧 Pré-requisitos

* Java 17+
* Maven

---

### ▶️ Backend

```bash
# Clone o repositório
git clone https://github.com/Sg-Gabriel/Screenmatch2.0.git

# Acesse o backend
cd Screenmatch2.0/Screenmatch2.0-back-end

# Execute a aplicação
./mvnw spring-boot:run
```

API disponível em:

```
http://localhost:8080
```

---

### 🌐 Frontend

```bash
# Acesse a pasta do frontend
cd Screenmatch2.0/Screenmatch2.0-front-end

# Abra o arquivo index.html no navegador
```

---

## 🔄 Integração Frontend ↔ Backend

O frontend consome diretamente os endpoints da API, como:

* `/series/top5`
* `/series/lancamentos`
* `/series`
* `/series/categoria/{genero}`

Os dados são processados e renderizados dinamicamente na interface.

---

## 📊 Possíveis Melhorias

* Autenticação com JWT
* Paginação de resultados
* Tratamento global de exceções
* Documentação com Swagger/OpenAPI
* Deploy (Render, Railway, etc.)

---

## 👨‍💻 Autor

**Gabriel Guimarães**
🔗 GitHub: https://github.com/Sg-Gabriel


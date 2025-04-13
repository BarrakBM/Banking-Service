# Banking Services Project

<aside>
⏰ Exercise **Details**
* Duration: **2 Days**
* Individual Assignment
</aside>

## 🚀 Project Overview

This is a Project exercise. We will build a small server that can provide some basic Banking Services using **Spring Boot** and **Kotlin**. The goal is to build a RESTful server that allows users to perform basic banking operations.

This exercise will help you practice:
* Creating Spring Boot projects from scratch.
* Designing and implementing REST APIs, clearly specifying endpoints (HTTP methods, request bodies, response structures).
* Handling validation and error scenarios, such as insufficient balance during transfers or duplicate usernames.
* Managing basic CRUD operations and business logic.
* Implementing transaction history to allow users to view past deposits, withdrawals, and transfers.

## 📌 Project Guidelines

* **Individual project**: Each student submits their own repository.
* **Start from scratch** using Spring Initializr.
* **Collaboration**: You can discuss concepts and share notes, but all code must be written independently.
* **Integrity**: Do not copy-paste others' code or use AI-generated code that you do not fully understand.

## 📊 Database Structure (ERD)

* This is an example ERD. Feel Free to create your own to match your back-end.
   * Some tools to help you:
      * https://online.visual-paradigm.com/diagrams/features/erd-tool/
      * https://app.diagrams.net/

## 🎯 User Stories (Requirements)

Your banking server must implement the following features:

1. As a User, I can register my username & password
2. As a User, I can create multiple accounts
3. As a User, I can put my KYC information in a profile
4. As a User, I can close my account
5. As a User, I can transfer money to another account

## ✅ Submission Checklist

* Functional API endpoints meeting all the user stories.
* Database schema matching the provided ERD.
* Well-organized, readable Kotlin code.
* Repository link submitted by the deadline.
* Follow Controller → Service → Repository pattern

## 🎉 **Happy Coding! Remember, the goal is to learn, experiment, and build confidence with Spring Boot and Kotlin. Ask questions and enjoy the journey!**

## API Specs

| Name | Method | Endpoint | Required Body | Response Schema |
|------|--------|----------|--------------|----------------|
| Register User | `POST` | `/users/v1/register` | `{ "username": String, "password": String }` | `200 OK - No Body` |
| List Accounts | `GET` | `/accounts/v1/accounts` | `No Body` | `{ "accounts": [Account] }` |
| Create New Account | `POST` | `/accounts/v1/accounts` | `{ "userId": Long, "name": String, "intialBalance": Decimal }` | `{ "userId": Long, "accountNumber": String, "name": String, "intialBalance": Decimal }` |
| Close Account | `POST` | `/accounts/v1/accounts/{accountNumber}/close` | `No Body` | `200 OK - No Body` |
| Transfer Funds to Another Account | `POST` | `/accounts/v1/accounts/transfer` | `{ "sourceAccountNumber": String, "destinationAccountNumber": String, "amount": Decimal }` | `200 OK { newBalance: Decimal }` |
| Get user KYC Information | `GET` | `/users/v1/kyc/{userId}` | `No Body` | `{ "userId": Long, "firstName": String, "lastName": String, "dataOfBirth": Date, }` |
| Create or Update KYC Information | `POST` | `/users/v1/kyc` | `{ "userId": Long, "firstName": String, "lastName": String, "dataOfBirth": Date, }` | `{ "userId": Long, "firstName": String, "lastName": String, "dataOfBirth": Date, }` |

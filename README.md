<p align="center">
  <strong>🚗 DomCar - Plataforma de Viajes Compartidos</strong><br>
  Aplicación web moderna y responsive para la gestión de viajes y reservas, desarrollada con Spring Boot y Tailwind CSS.
</p>

<p align="center">
  <a href="#"><img alt="Java" src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"></a>
  <a href="#"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.14-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"></a>
  <a href="#"><img alt="Thymeleaf" src="https://img.shields.io/badge/Thymeleaf-3.1.4.RELEASE-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"></a>
  <a href="#"><img alt="Tailwind CSS" src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white"></a>
  <a href="#"><img alt="MySQL" src="https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white"></a>
</p>

---

## 📖 Acerca del Proyecto

**DomCar** es una plataforma orientada a facilitar la gestión de viajes compartidos. Los usuarios pueden consultar rutas, plazas disponibles y realizar reservas de forma rápida e intuitiva. 

El proyecto destaca por su arquitectura **MVC**, persistencia robusta con JDBC, protección contra errores comunes de lógica y una interfaz moderna *Mobile First* implementada con Tailwind CSS.

> ⚠️ **Nota del Autor**
> 
> Este fue mi **primer proyecto serio como estudiante de programación**. Aunque ha sido remodelado y mejorado en algunos aspectos, no ha sido completamente refactorizado con los conocimientos que tengo actualmente. De haber tenido más experiencia en ese momento, muchas decisiones de arquitectura y patrones de código habrían sido diferentes. Es un proyecto que refleja mi crecimiento como desarrollador, pero hay que tener en cuenta que una reescritura desde cero aplicando mejores prácticas, sería mucho más conveniente. Lo comparto como referencia de aprendizaje y de lo que he aprendido que como un proyecto "serio".

## ✨ Características Principales

- **Gestión de Viajes:** Listado interactivo con búsqueda por destino, creación y cancelación de viajes.
- **Sistema de Reservas:** Asignación segura de plazas con identificadores únicos (`UUID`).
- **Control de Estado:** Los viajes cambian de estado de forma dinámica (Abierto, Cerrado, Cancelado).
- **Diseño Responsive:** Interfaz atractiva y fluida adaptable a cualquier dispositivo gracias a Tailwind CSS.

## 🚀 Cómo Empezar

### 1. Requisitos
- **Java 21+**
- **MySQL 8.0+**
- El proyecto incluye Maven Wrapper, no necesitas Maven instalado

### 2. Base de Datos
Crea la base de datos `domcar` en MySQL:
```bash
mysql -u root -p
mysql> CREATE DATABASE domcar;
mysql> EXIT;
```

Spring Boot inicializará automáticamente las tablas y datos de prueba al arrancar la aplicación usando el script `src/main/resources/database/import.sql`.

### 3. Configuración
Edita `src/main/resources/application.properties` con tus credenciales de MySQL:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/domcar
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA
```

### 4. Ejecución
Inicia la aplicación:
```bash
# Windows
.\mvnw spring-boot:run

# Linux / Mac
./mvnw spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

## 🎨 Desarrollo Frontend (Tailwind CSS)
El proyecto utiliza Tailwind CSS (v4 CLI). Si deseas modificar los estilos, no edites `style.css` directamente. Añade tus clases al HTML o a `input.css` y compila usando npm:

```bash
# Instalar dependencias
npm install

# Vigilar y compilar cambios en vivo
npm run watch:css
```

---

<p align="center">
  Desarrollado con ❤️ y ☕ por <a href="https://github.com/martinezdom">Miguel Ángel Martínez</a>.
</p>

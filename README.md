# 🌦️ Weather Tracker

A web application to view and manage your favorite locations’ weather in real-time. Users can sign up, log in, search for locations, save them to their personal dashboard, and see current weather conditions from OpenWeather API.

---

## 🧩 Tech Stack

- **Java 21**
- **Spring MVC**  
- **Thymeleaf** for server-side HTML rendering  
- **Hibernate ORM (JPA)**  
- **PostgreSQL** as the main database  
- **Flyway** for schema migrations  
- **HikariCP** for connection pooling  
- **OpenWeather API** for live weather data  
- **Bootstrap** for responsive UI  
- **Maven** for build management  
- **Tomcat 11** as the servlet container  

---

## 🖥️ How It Works

### 🔑 Authentication  
- **Sign up** → Creates a new user record with hashed password  
- **Sign in** → Issues session cookie stored in DB with expiration  
- **Logout** → Removes session + cookie  

### 📍 Locations  
- Search locations via `/search-results`  
- Add a location → saves to user’s `Locations` table  
- Homepage `/` shows saved locations with weather cards  
- Delete location with the ❌ icon  

### 🌦️ Weather Data  
- Fetched from OpenWeather API  
- Cached only per page render, always fresh at request time  
- Units in **metric (°C)** by default  

---

## 🧰 How to Run

### 1️⃣ **Prerequisites**
- **Java 17+** installed  
- **PostgreSQL** running locally  
- **Tomcat 11+** installed  
- **OpenWeather API Key** ([get it here](https://openweathermap.org/api))  

### 2️⃣ **Configure Database and API Key**
Create a PostgreSQL database:
```sql
CREATE DATABASE weather_db;
```
Configure credentials **either** via the `application.properties` file **or** via environment variables.

#### Option 1 — Edit `src/main/resources/application.properties`

```properties
db.username=YOUR_DB_USERNAME
db.password=YOUR_DB_PASSWORD
weather.api.key=YOUR_OPENWEATHER_API_KEY
```

#### Option 2 — Use Environment Variables (recommended for production)

**Linux / macOS (bash/zsh):**
```bash
export DATABASE_USERNAME='YOUR_DB_USERNAME'
export DATABASE_PASSWORD='YOUR_DB_PASSWORD'
export WEATHER_API_KEY='YOUR_OPENWEATHER_API_KEY'
```

**Windows (Command Prompt):**
```cmd
set DATABASE_USERNAME=YOUR_DB_USERNAME
set DATABASE_PASSWORD=YOUR_DB_PASSWORD
set WEATHER_API_KEY=YOUR_DB_PASSWORD
```

### 3️⃣ **Build the Project**
```bash
mvn clean package
```

### 4️⃣ **Deploy to Tomcat**
- Copy the generated WAR from `target/weather-tracker.war` into Tomcat’s `webapps` folder  
- Start Tomcat:
```bash
$CATALINA_HOME/bin/startup.sh
```
- App available at [http://localhost:8080/weather-tracker/](http://localhost:8080/weather-tracker/)  

### 5️⃣ **First Run**
- Flyway runs automatically to create tables  
- Go to `/sign-up` to create your first account  
- Start adding locations and view their weather!  

---

## 📝 Credits
This project was developed following the [zhukovsd/java-backend-learning-course](https://zhukovsd.github.io/java-backend-learning-course/) roadmap.  
Weather data powered by [OpenWeather](https://openweathermap.org/).

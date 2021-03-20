# Ticket Management System
A Spring boot based backend REST application for handling all APIs concerning the whole Ticket Management Application

### Spring Profile Handling
1. **Local:** Saved 'dev' as SPRING_PROFILES_ACTIVE env variable in edit config
2. **Heroku:** Saved 'uat' as SPRING_PROFILES_ACTIVE config variable on heroku

### Deployments
-   uat branch deployed(CI/CD) on heroku at [https://sg-tms.herokuapp.com/](https://sg-tms.herokuapp.com/)
    
###Todos
-   Make creation of users specific to SUPER_ADMIN
-   Migrate from ***Long*** based IDs to **UUID** based IDs
-   Ticket Controller
-   Add AOP based auditing (probably annotated)
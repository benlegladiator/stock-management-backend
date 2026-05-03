# Déploiement du backend Spring Boot sur Heroku

## 1. Initialisation Git
```bash
cd c:\Users\PC\Desktop\projet_java\stock-management-backend
git init
git add .
git commit -m "Initial commit - Backend Spring Boot"
```

## 2. Créer l'application Heroku
```bash
heroku create ton-backend-unique-name
```

## 3. Ajouter la base de données PostgreSQL
```bash
heroku addons:create heroku-postgresql:hobby-dev
```

## 4. Déployer sur Heroku
```bash
git subtree push --prefix stock-management-backend heroku main
```

## 5. Initialiser les données
```bash
heroku run curl -X POST https://ton-backend-unique-name.herokuapp.com/api/init
```

## 6. Vérifier le déploiement
```bash
heroku open
curl https://ton-backend-unique-name.herokuapp.com/api/dashboard/stats
```

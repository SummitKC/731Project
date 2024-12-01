Project Structure:

/731Project
├── backend
└── frontend

/731Project/Backend
  ├── src
  │   ├── main
  |   |     └── ...
  │   ├── test
  |   |     └── ...
  └── ...

/731Project/frontend
  ├── public
  │   ├── index.html
  │   ├── ...
  ├── src
  |   ├── assets
  |   |   ├── style.css
  |   |   ├── image.jpg
  |   |   ├── ...
  │   ├── components
  │   │   ├── Header.jsx
  │   │   ├── ...
  │   ├── pages
  │   │   ├── StudentRegister.js
  │   │   ├── LoginPage.js
  |   |   |__...
  │   ├── services
  │   │   ├── api.js
  │   │   ├── ...
  │   ├── AppRouter.js
  │   ├── index.js
  │   ├── ...
  ├── package.json
  └── ...

#To start the app


##IMPORTANT: Make sure you are on Java version 21 

To run the app please download these packages in the Frontend folder:

- npm install -g npm@10.9.0
- npm install --save react-responsive
- npm install react-router-dom
- npm install date-fns
- npm install react-beautiful-dnd

To start the front end CD into the Frontend, and type in "npm start" in the terminal. NOTE this only starts the frontend. 

To start the backend, CD into the Backend folder, and type in `./mvnw spring-boot:run -D"spring-boot.run.profiles=dev"` in the terminal. NOTE this only stats the backend. 

Once both are started, the app should be functional. 

FROM node:lts-alpine3.13

WORKDIR /app

COPY package*.json ./

RUN npm install
COPY . .

CMD ["node", "src/server.js"]

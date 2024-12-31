# Use the official node image to build the frontend
FROM node:18 AS builder

WORKDIR /app

# Copy the package.json and install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy source code and build the app
COPY . /app
RUN npm run build --prod

# Use an Nginx image to serve the built frontend
FROM nginx:alpine

# Copy the built frontend files into the Nginx container
COPY --from=builder /app/dist/frontend /usr/share/nginx/html

# Expose the port Nginx runs on
EXPOSE 80

# Start Nginx to serve the app
CMD ["nginx", "-g", "daemon off;"]

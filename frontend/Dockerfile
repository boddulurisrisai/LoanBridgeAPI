# Use Node.js for building the Angular app
FROM node:18 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the entire application code
COPY . .

# Build the application for production
RUN npm run build --prod

# Use NGINX to serve the static files
FROM nginx:alpine

# Copy the build files to NGINX's serving directory
COPY --from=build /app/dist/loan-eligibility /usr/share/nginx/html

# Expose the port NGINX serves on
EXPOSE 4200

# Start NGINX
CMD ["nginx", "-g", "daemon off;"]

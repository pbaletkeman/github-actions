FROM node:20-alpine

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm ci --only=production

# Copy TypeScript and source
COPY tsconfig.json .
COPY src/ ./src/

# Build TypeScript
RUN npm install -D typescript ts-node @types/node && npm run build

# Create non-root user
RUN addgroup -g 1000 nodegroup && adduser -D -u 1000 -G nodegroup nodeuser
RUN chown -R nodeuser:nodegroup /app
USER nodeuser

# Expose default port (can be overridden)
EXPOSE 3000

# Run
ENTRYPOINT ["node", "dist/main.js"]
CMD ["--help"]

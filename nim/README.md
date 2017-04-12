# Nim

- NimClient.java handles keyboard input from the user
- NimClientListener.java receives responses from the server and displays them
- NimServer.java listens for client connections and creates a ClientHandler for each new client
- NimClientHandler.java receives messages from a client and relays it to the other client(s)

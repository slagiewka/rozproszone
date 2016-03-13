#include <sys/socket.h>
#include <netinet/in.h>
#include <stdio.h>
#include <string.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <stdint.h>
#include <fcntl.h>
#include <unistd.h>
#include <stdint-gcc.h>
#include <netinet/in.h>
#include <string.h>
#include <bits/socket.h>
#include <arpa/inet.h>

#define BUFFER_SIZE 256

#define errorHandler(statement, error) \
if ((statement) == -1) { \
    perror((error)); \
    exit(EXIT_FAILURE); \
}

void sendFileName(char const *filename, int socketDescriptor);

void sendFileContent(int fileDescriptor, int socketDescriptor);

int openFile(char const *filename);

struct sockaddr_in getSocketAddressFor(char const *serverAddressArg, uint16_t serverPort);

void connectToServer(int socketDescriptor, struct sockaddr_in *serverAddress);

int createSocket();

void closeDescriptor(int descriptor);
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
#include <stdbool.h>

#define errorHandler(statement, error) \
if ((statement) == -1) { \
    perror((error)); \
    exit(EXIT_FAILURE); \
}

#define htonll(x) ((((uint64_t)htonl(x)) << 32) + htonl((x) >> 32))

struct sockaddr_in getSocketAddressFor(char const *serverAddressArg, uint16_t serverPort);

void connectToServer(int socketDescriptor, struct sockaddr_in *serverAddress);

int createSocket();

void closeDescriptor(int descriptor);

void sendNoBytes(int descriptor, unsigned char numberOfBytes);

void sendNumberAsNBytes(int descriptor, long long number, unsigned char numberOfBytes);

void waitForAnswer(int descriptor);

void readUserInput(unsigned char *nbytes, long long int *number);

bool handleValidInput(unsigned char nbytes, long long int number);

void intHandler(int signum);
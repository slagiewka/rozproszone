#include <signal.h>
#include "client.h"

bool volatile noInterruption = true;

int main(int argc, char **argv) {

    const char *serverAddressArg;
    uint16_t serverPort;

    if (argc != 3) {
        printf("Client expects exactly 2 arguments: ./client <IP address> <port>\n");
        exit(EXIT_FAILURE);
    }

    serverAddressArg = argv[1];
    serverPort = (uint16_t) atoi(argv[2]);

    signal(SIGINT, intHandler);

    int socketDescriptor = createSocket();
    struct sockaddr_in serverAddress = getSocketAddressFor(serverAddressArg, serverPort);
    connectToServer(socketDescriptor, &serverAddress);

    unsigned char nbytes;
    long long number;
    while (noInterruption) {
        readUserInput(&nbytes, &number);
        if (!handleValidInput(nbytes, number)) {
            printf("Invalid values, please try again!\n");
            continue;
        }
        sendNoBytes(socketDescriptor, nbytes);
        sendNumberAsNBytes(socketDescriptor, number, nbytes);
        waitForAnswer(socketDescriptor);
    }

    closeDescriptor(socketDescriptor);

    return 0;
}

void intHandler(int signum) {
    noInterruption = false;
}

bool handleValidInput(unsigned char nbytes, long long int number) {
    bool areBytesInRange = nbytes == 1 || nbytes == 2 || nbytes == 4 || nbytes == 8;
    bool isNumberInRange = number > 0 && number < (2llu << ((nbytes * 8 - 1) - 1));

    return areBytesInRange && isNumberInRange;
}

void readUserInput(unsigned char *nbytes, long long int *number) {
    printf("Please specify number of bytes {1, 2, 4, 8} and number: ");
    scanf("%hhu %lld", nbytes, number);
}

void waitForAnswer(int descriptor) {
    unsigned char result;
    errorHandler(
            recv(descriptor, &result, sizeof result, 0),
            "Error in recv: "
    );

    printf("Response: %d\n", (int)result);
}

void sendNumberAsNBytes(int descriptor, long long number, unsigned char numberOfBytes) {
    long long networkRepr = htonll(number);
    long long shiftedNetworkRepr =
            networkRepr >> 8 * (sizeof(long long int) - numberOfBytes);

    errorHandler(
            send(descriptor, &shiftedNetworkRepr, numberOfBytes, 0),
            "Error in send: "
    );
}

void sendNoBytes(int descriptor, unsigned char numberOfBytes) {
    errorHandler(
            send(descriptor, &numberOfBytes, sizeof numberOfBytes, 0),
            "Error in send: "
    );
}

void closeDescriptor(int descriptor) {
    errorHandler(
            close(descriptor),
            "Error in close: "
    )
}

int createSocket() {
    int socketDescriptor;
    errorHandler(
            (socketDescriptor = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)),
            "Error in socket:"
    );
    return socketDescriptor;
}

void connectToServer(int socketDescriptor, struct sockaddr_in *serverAddress) {
    errorHandler(
            connect(socketDescriptor, (struct sockaddr *) serverAddress, sizeof(*serverAddress)),
            "Error in connect_to: "
    );

}

struct sockaddr_in getSocketAddressFor(char const *serverAddressArg, uint16_t serverPort) {
    struct sockaddr_in serverAddress;
    memset(&serverAddress, 0, sizeof(serverAddress));

    serverAddress.sin_family = AF_INET;
    serverAddress.sin_addr.s_addr = inet_addr(serverAddressArg);
    serverAddress.sin_port = htons(serverPort);
    return serverAddress;
}
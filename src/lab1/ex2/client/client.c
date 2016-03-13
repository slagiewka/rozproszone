#include "client.h"

int main(int argc, char **argv) {

    const char *serverAddressArg, *filename;
    uint16_t serverPort;

    if (argc != 4) {
        printf("Client expects exactly 4 arguments: ./client <IP address> <port> <filename>\n");
        exit(EXIT_FAILURE);
    }

    serverAddressArg = argv[1];
    serverPort = (uint16_t) atoi(argv[2]);
    filename = argv[3];

    int socketDescriptor = createSocket();
    struct sockaddr_in serverAddress = getSocketAddressFor(serverAddressArg, serverPort);
    connectToServer(socketDescriptor, &serverAddress);
    int fileDescriptor = openFile(filename);

    sendFileName(filename, socketDescriptor);
    sendFileContent(fileDescriptor, socketDescriptor);

    closeDescriptor(fileDescriptor);
    closeDescriptor(socketDescriptor);

    return 0;
}

void closeDescriptor(int descriptor) {
    errorHandler(close(descriptor),"Error in close: ");
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
            "Error in connectToServer: "
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

int openFile(char const *filename) {
    int fileDescriptor;

    errorHandler(
            (fileDescriptor = open(filename, O_RDONLY)),
            "Error in open: "
    )

    return fileDescriptor;
}

void sendFileName(char const *filename, int socketDescriptor) {
    unsigned char filenameLength = (unsigned char) strlen(filename);
    errorHandler(
            send(socketDescriptor, &filenameLength, sizeof filenameLength, 0),
            "Error in send: "
    );

    errorHandler(
            send(socketDescriptor, filename, strlen(filename), 0),
            "Error in send: "
    );

}

void sendFileContent(int fileDescriptor, int socketDescriptor) {
    char buffer[BUFFER_SIZE];
    ssize_t bytesRead;

    errorHandler(
            (bytesRead = read(fileDescriptor, &buffer, sizeof buffer)),
            "Error in read: "
    );

    while (bytesRead != 0) {
        errorHandler(
                send(socketDescriptor, &buffer, bytesRead, 0),
                "Error in send: "
        );

        errorHandler(
                (bytesRead = read(fileDescriptor, &buffer, sizeof buffer)),
                "Error in read: "
        );
    }
}

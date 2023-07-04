import asyncio
import json

import websockets

SESSIONS = {'teacher': dict(),
            'student': dict()}


async def send_file(websocket, filename):
    with open(filename, "rb") as f:
        inf = f.readline()
        while inf:
            await websocket.send(inf)
            inf = f.readline()
        await websocket.send("None")


async def get_file(websocket, filename):
    with open(filename, "rb") as f:
        inf = f.read(1024)
        while inf:
            await websocket.send(inf)
            inf = f.read(1024)
        await websocket.send("None")


async def handler(websocket):
    print("Start")
    global SESSIONS
    f = None
    while True:
        try:
            message = await websocket.recv()
        except websockets.ConnectionClosedOK:
            break
        if message == "file":
            f = open("index.jpg", "wb")
        elif not(f is None):
            f.write(message)
        elif f == "None":
            f.close()
            f = None

    print("Close")


async def main():
    async with websockets.serve(handler, "localhost", 8001):
        await asyncio.Future()


if __name__ == "__main__":
    asyncio.run(main())

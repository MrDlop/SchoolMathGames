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
    while True:
        try:
            message = await websocket.recv()
        except websockets.ConnectionClosedOK:
            break
        await send_file(websocket, "index1.jpg")
        print(message)
        mess = json.loads(message)
        print(mess)
        if mess["type_request"] == "TASK":
            if mess["type_data"] == "file":
                for student in mess["student"]:
                    await send_file(SESSIONS['student'][student], mess["filename"])
            else:
                for student in mess["student"]:
                    await SESSIONS['student'][student].send(mess["text_task"])
        elif mess["type_request"] == "POST":
            for teacher in SESSIONS['teacher']:
                await teacher.send(mess)
        elif mess["type_request"] == "CONNECT":
            if mess["name"] in SESSIONS[mess["type_man"]]:
                await websocket.send("TEAM_0")  # team already create
            else:
                SESSIONS[mess['type_man']][mess["name"]] = websocket
                await websocket.send("TEAM_1")  # team created
                await websocket.send("OK")
        elif mess["type_request"] == "DISCONNECT":
            del SESSIONS[mess['type_man']][mess["name"]]
            break
    print("Close")


async def main():
    async with websockets.serve(handler, "localhost", 8001):
        await asyncio.Future()


if __name__ == "__main__":
    asyncio.run(main())

#!/usr/bin/env python

# standard libraries
import os
import sys
import socket
import json
import select
import errno
import random
from time import sleep

# 3rd party libraries

# local libraries
import macro


class Master_Client(object):
    def __init__(self, destination, port=5005, buffer_size=1024):
        # master class variable
        self.destination = destination
        self.port = port
        self.buffer_size = buffer_size

        # server configuration variable
        self.server_acceptable_priority = 9
        self.connected = False

        self.sock = None
        self.server_address = (self.destination, self.port)
        # connect to the server
        self.connect()

    # https://pymotw.com/2/socket/tcp.html
    def connect(self):
        # Create a TCP/IP socket
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # Connect the socket to the port where the server is listening
        self.sock.connect(self.server_address)
        self.sock.setblocking(0)
        print ("Connected")
        self.connected = True

    def disconnect(self):
        self.sock.close()
        print ("Disconnected")
        self.connected = False

    def send_data(self, operation, packet_priority, data):
        if self.connected:
            try:
                # if the packet priority is within server accept priority range
                if packet_priority <= self.server_acceptable_priority:
                    # Send data
                    encoded_data = json.dumps({"operation": operation, "priority": packet_priority,
                                               "data": data
                                               }) + "\n"
                    self.sock.sendall(encoded_data)
                    print ("Sending data")
            except socket.error, e:
                return

                # http://stackoverflow.com/questions/16745409/what-does-pythons-socket-recv-return-for-non-blocking-sockets-if-no-data-is-r

    def receive_data(self):
        # receive a package
        try:
            message = self.sock.recv(1024)
            return self.process_received_message(message)
        except socket.error, e:
            err = e.args[0]
            if err == errno.EAGAIN or err == errno.EWOULDBLOCK:
                sleep(0.001)
                return None

    #should contain a callback
    def process_received_message(self, received_messages):
        # decode the received message
        for received_message in received_messages.split("\n"):
            try:
                decoded_data = json.loads(received_message)
                # if the packet received
                if decoded_data["operation"] == "set_priority":
                    if "priority" in decoded_data.keys():
                        self.set_priority(decoded_data["priority"])

                return decoded_data
            except ValueError:
                print ("####### ERR ########")
                print ("Undecoded message " + received_message)
                print ("###############")
        return None

    def set_priority(self, new_threshold):
        print("Priority changed from " + str(self.priority) + " -> " + str(new_threshold))
        self.server_acceptable_priority = new_threshold
        # def set_threshold(self, new_threshold):
        # 	self.threshold = new_threshold


    def random_hello_packet(self):
        random_chance = random.uniform(0,99999)
        if(random_chance < 10):
            self.send_data("hello", 9, "")

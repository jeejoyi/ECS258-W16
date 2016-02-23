#!/usr/bin/env python

#standard libraries
import os
import sys
import socket
import json

#3rd party libraries

#local libraries
import macro

class Master_Client(object):
	def __init__(self, destination, port = 5005, buffer_size = 1024, priority = macro.PRIORITY_LOW):
		# master class variable
		self.destination = destination
		self.port = port
		self.buffer_size = buffer_size
		self.priority = priority
		self.min_threshold = None
		self.max_threshold = None

		self.sock = None
		self.server_address = (self.destination, self.port)
		print("here")
		# connect to the server
		self.connect()

	# https://pymotw.com/2/socket/tcp.html
	def connect(self):
		# Create a TCP/IP socket
		self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

		# Connect the socket to the port where the server is listening
		self.sock.connect(self.server_address)

	def disconnect(self):
		self.sock.close()

	def send_data(self, message):
		# try:
			# Send data
		encoded_data = json.dumps(message)
		self.sock.sendall(encoded_data)
		# except:
		# 	raise AssertionError

	def receive_data(self):
		# receive a package
		data = self.sock.recv(self.buffer_size)
		# if the message in the package is just ack, ignore it
		if data == "ACK":
			return None
		else: #if something other than ACK, return it so sensor can process the message
			return data

	def set_priority(self, new_proiority):
		self.priority = new_proiority

	def set_threshold(self, new_threshold):
		self.threshold = new_threshold
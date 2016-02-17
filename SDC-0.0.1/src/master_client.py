#!/usr/bin/env python

#standard libraries
import os
import sys
import socket

#3rd party libraries

#local libraries
import macro

class Master_Client(object):
	def __init__(self, destination, port = 5005, buffer = 1024, priority = macro.PRIORITY_LOW):
		self.destination = destination
		self.port = port
		self.buffer_size = buffer
		self.priority = priority
		self.threshold = None

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

	def disconnect(self):
		self.sock.close()

	def send_data(self, message):
		# try:
			# Send data
		self.sock.sendall(message)
		# except:
		# 	raise AssertionError

	def receive_data(self):
		pass
		# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		# s.bind((self.destination, self.port))
		# s.listen(1)

		# conn, addr = s.accept()

		# data = conn.recv(BUFFER_SIZE)
		# if data:
		# 	conn.send(data)  # echo
		# conn.close()

	def set_priority(self, new_proiority):
		self.priority = new_proiority

	def set_threshold(self, new_threshold):
		self.threshold = new_threshold
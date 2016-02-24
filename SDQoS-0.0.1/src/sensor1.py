#!/usr/bin/env python

#standard libraries
import os
import sys
import random

#3rd party libraries

#local libraries
import macro
from master_client import Master_Client

# sensor1 inherited Master_client
class Sensor1(Master_Client):
	def __init__(self, min_threshold, max_threshold):
		Master_Client.__init__(self, "127.0.0.1")
		self.min_threshold = min_threshold
		self.max_threshold = max_threshold
		self.send_rate = None
	
	def detection(self):
		# this code can by sensor input
		# random distance
		# sample random distance from 1cm to 200cm
		if random.randint(-2000000,2000000) > 0:
			return True

		return False

	def apply_policy(self, input_data):
		# if the detection distance is > 0 and < 200 true else false
		if self.min_threshold < input_data < self.max_threshold:
			return {"priority": self.priority, "data": float(input_data)}, True
		return None, False

	def decision(self, input_data):
		# apply policy to input data from sensor
		filtered_data, process = self.apply_policy(input_data)
		if process and filtered_data != None:
			#send filtered data
			self.send_data(filtered_data)

		received_message = self.receive_data()
		if received_message != None:
			self.process_received_message(received_message)

	def process_received_message(self, received_message):
		# do nothing for now
		print(received_message)
		# decode the received message
		decoded_data = json.loads(received_message)
		# update assoicate value
		for var_name, value in decoded_data:
			try:
				eval("self.{name} = {val}".format(name = var_name, val = value))
			except NameError:
				# error on var_name, just ignore it
				pass

	def run(self):
		while(1):
			try:
				# try capture data from sensor
				captured_data = self.detection()
				print("hi")
				# if captured data from sensor
				if captured_data:
					self.decision(captured_data)

				#check if theres any data received from the server side
				# self.receive_data()
			except KeyboardInterrupt:
				break

if __name__ == '__main__':
	s1 = Sensor1(0, 200)
	try:
		print("here")
		s1.run()
	finally:
		s1.disconnect()






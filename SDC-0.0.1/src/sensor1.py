#!/usr/bin/env python

#standard libraries
import os
import sys


#3rd party libraries

#local libraries
import macro
from master_client import Master_Client

class Sensor1(Master_Client):
	def __init__(self):
		Master_Client.__init__(self, "127.0.0.1")

	def detection(self):
		return True

	def apply_policy(self, input_data):
		return None, False

	def decision(self, input_data):
		# apply policy to input data from sensor
		filtered_data, process = self.apply_policy(input_data)
		if process and filtered_data != None:
			self.send_data("hello")

	def run(self):
		while(1):
			try:
				# try capture data from sensor
				captured_data = self.detection()
				# if captured data from sensor
				if captured_data:
					self.decision(captured_data)

				#check if theres any data received from the server side
				# self.receive_data()
			except KeyboardInterrupt:
				break

if __name__ == '__main__':
	s1 = Sensor1()
	try:
		s1.run()
	finally:
		s1.disconnect()






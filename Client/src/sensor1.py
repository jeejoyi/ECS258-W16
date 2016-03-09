#!/usr/bin/env python

# standard libraries
import os
import sys
import random
import time
import string

# 3rd party libraries

# local libraries
import macro
from master_client import Master_Client


# sensor1 inherited Master_client
class Sensor1(Master_Client):
    def __init__(self, ip_address, priority, time_max_threshold, message_len_lambda, min_threshold, max_threshold):
        Master_Client.__init__(self, ip_address, priority=priority)
        self.time_max_threshold = time_max_threshold
        self.message_len_lambda = message_len_lambda

        self.min_threshold = min_threshold
        self.max_threshold = max_threshold

    def random_pause_time(self):
        # generate a random number by uniform distribution
        pause_seconds = random.uniform(0, self.time_max_threshold)
        time.sleep(pause_seconds)

    def generate_random_data(self):
        # generate random message lenght by exp
        random_length = random.expovariate(self.message_len_lambda)
        message = ''.join(random.choice(string.ascii_uppercase + string.digits) for _ in range(int(random_length)))
        # print(random_length, message)
        return message

    def detection(self):
        # this code can by sensor input
        # random distance
        # sample random distance from 1cm to 200cm
        distance = random.uniform(0, 200000)
        return distance

    def apply_policy(self, input_data):
        # if the detection distance is > 0 and < 200 true else false
        if self.min_threshold < input_data < self.max_threshold:
            return float(input_data), True
        return None, False

    def decision(self, input_data):
        # apply policy to input data from sensor
        filtered_data, process = self.apply_policy(input_data)
        if process and filtered_data != None:
            # send filtered data
            # self.send_data(filtered_data)
            self.send_data(self.generate_random_data())
            self.random_pause_time()

        # received_message = self.receive_data()
        # if received_message != None:
        # 	self.process_received_message(received_message)

    def update_setting(self, message):
        if message and "operation" in message and message["operation"] == "w":
            setting = message["data"]

    def run(self):
        while (1):
            try:
                # try capture data from sensor
                captured_data = self.detection()
                # print(captured_data)
                # if captured data from sensor
                if captured_data:
                    self.decision(captured_data)

                # check if theres any data received from the server side
                self.update_setting(self.receive_data())
            except KeyboardInterrupt:
                break


if __name__ == '__main__':
    if len(sys.argv) != 5:
        print("Usage: python sensor1.py <address> <priority> <uniform time range> <lambda>\n")
        print("Eg:\npython ./sensor1.py localhost 2 2 0.00001")
        sys.exit(-1)

    ip_address = sys.argv[1]
    priority = int(sys.argv[2])
    uniform_pause_time_threshold = int(sys.argv[3])
    exponential_lambda = float(sys.argv[4])

    s1 = Sensor1(ip_address, priority, uniform_pause_time_threshold, exponential_lambda, 0, 200)
    try:
        s1.run()
    finally:
        s1.disconnect()

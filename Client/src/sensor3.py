#!/usr/bin/env python

# standard libraries
import math
import random
import string
import sys
import time

# 3rd party libraries

# local libraries
from master_client import Master_Client


# sensor1 inherited Master_client
class Sensor1(Master_Client):
    def __init__(self, ip_address, time_max_threshold, message_len_lambda):
        Master_Client.__init__(self, ip_address)
        self.time_max_threshold = time_max_threshold
        self.message_len_lambda = message_len_lambda

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

    @staticmethod
    def sensor_operation():
        return random.uniform(0, 99999)

    @staticmethod
    def apply_policy(sample_data):
        # if within range
        if 0 < sample_data < 200:
            return True

        return False

    def update_sensor_setting(self, new_setting):
        # currently update nothing since this is just a simulator
        pass

    def run(self):
        while True:
            # try:
                # sample detection
            sample_data = self.sensor_operation()
            # if within the setting range
            if self.apply_policy(sample_data):
                # random priority
                random_priority = 10 - int(math.ceil(math.log(random.random()*1024, 2)))
                self.send_data("data", random_priority, self.generate_random_data())
                self.random_pause_time()

            # listen to see if theres any update
            self.update_sensor_setting(self.receive_data())
            self.random_hello_packet()
            # except:
            #     break

if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("Usage: python sensor3.py <address> <uniform time range> <lambda>\n")
        print("Eg:\n python ./sensor3.py localhost 1 0.000025")
        # python ./sensor3.py localhost 1 0.000025
        sys.exit(-1)

    ip_address = sys.argv[1]
    uniform_pause_time_threshold = float(sys.argv[2])
    exponential_lambda = float(sys.argv[3])

    s1 = Sensor1(ip_address, uniform_pause_time_threshold, exponential_lambda)
    try:
        s1.run()
    finally:
        s1.disconnect()
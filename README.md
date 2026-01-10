# Farm Fusion — All-in-One Smart Farming Vehicle
Farm Fusion is a multi-functional farming vehicle designed to automate major agricultural tasks through a mobile application. The system combines embedded electronics, motor control, and on-device AI to support modern, efficient farming. The goal is to make automation affordable and accessible for farmers by integrating multiple tools into a single machine.
Project Overview
# Farm Fusion brings together several hardware components to perform different farming operations:
•	Raspberry Pi 5 for running AI-based weed detection

•	ESP32 for motor, servo, and tool control

•	Hub motors for vehicle movement

•	Servo mechanisms for spraying, weeding, seeding, and harvesting

•	Bluetooth connectivity between the Android app and ESP32

•	Android app built using Java and XML
# Android App (Java + XML)
The Android application provides real-time manual control of the farming vehicle.

•	Communicates with the ESP32 using Bluetooth RFCOMM

•	Sends commands for vehicle movement, wheel rotation, tool activation, and emergency stop

•	Receives updates such as tool status and warnings

•	Includes safety alerts using Toast messages

•	Maintains continuous two-way communication for smooth operation
# AI-Based Weed Detection (Raspberry Pi 5)
A camera module captures live field images, which are processed on the Raspberry Pi.
The AI module:

•	Utilizes a lightweight model such as YOLOv8-Nano, suitable for edge devices

•	Analyses every camera frame to identify weeds with good accuracy

•	Activates the weed-removal tool only when a weed is correctly detected

•	Helps reduce chemical usage and avoids harming healthy crops

This feature brings precision farming directly onto the field.
# ESP32 Hardware Controller
The ESP32 handles all hardware-level tasks of the vehicle.

•	Controls hub motors for direction and motion

•	Operates servo motors for individual tools

•	Interprets Bluetooth commands from the Android app

•	Sends feedback messages back to the app

•	Uses separate GPIO pins for each module, making the design modular and easy to extend
# Project Goals
Farm Fusion focuses on:

•	Reducing manual labour by combining multiple farming functions

•	Providing a cost-effective and modular automation solution

•	Supporting real-time mobile interaction from the field

•	Improving precision through AI-based weed detection

•	Bringing multiple smart farming operations into a single compact system


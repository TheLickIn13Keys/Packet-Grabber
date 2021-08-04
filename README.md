# Packet-Grabber
Made for ACSEF Science Fair and ***Won 2nd Place***

When run, the program initiates a man in the middle attack and collects all network traffic going to and from the router. Then, it displays all images that were sent over http in a driftnet window.

In order for the program to run, you must get a token from https://the-token-master.glitch.me/

NOTE: You have to specify the network interface you want to use on line 48 of App.js in place of enp10s0, e.g. eth0.

Prerequisites:

- Ettercap
- Driftnet

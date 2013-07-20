DiscoSheep
==========

DiscoSheep plugin for Craftbukkit. It is written for API 1.5.2-R1.0 but it is expected to work with 1.6.
This is a from-scratch remake of the fabulous DiscoSheep plugin which spawns a dance party of sheep at your whim.

Download latest build
--------
https://github.com/KotGaming/DiscoSheep/blob/develop/dist/DiscoSheep.jar (use at your own risk!)

### Configuration File ###
(should be self explanatory)

	max:
		sheep: 100
		radius: 100
		duration: 120
		period-ticks: 40
	min:
		period-ticks: 5
	default:
		sheep: 10
		radius: 5
		duration: 15
		period-ticks: 10
		party-on-join: true

### Permission Nodes ###

	discosheep.party - Allows a player to have a party of one
	discosheep.partyall - Allows a player to call a server-wide party
	discosheep.stop - Allows a player to stop all parties on the server
	discosheep.fireworks - Allows a player to enable have parties with fireworks
	discosheep.reload - Allows a player to reload settings from config.yml
	discosheep.onjoin - Allows a player to have a party when joining

### License ###
Copyright (c) 2013 "Gibstick", "RangerMauve"

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

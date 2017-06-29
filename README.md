# FlugProject

This is an application which I made in an Aviation-Hackathon. (17.06.17 - in Berlin)
Our team (AirportShopper) did a project for shoppers who do not have enough time to buy what they need before their flights.
Our team consisted of one back-end developer, one front-end developer (me), one designer, one idea generator and the other who studies Learning-machine.
Our aim : The travelers can get recommendations what they need and can shop while waiting in the long-lines in airports.
	They select their flights in the flights' list. When they arrive at the destinations they can get what they ordered before departures.

1. Tutorial -> Explains the users how they can use our application and the aim of the application.
2. Detects beacons nearby the users -> I could use a beacon which was supported by Estimote by using a library (Estimote).
				 I confirmed that the application could detect beacons nearby.
3. Sends device's uuid and beacon's id to the beacon.
				-> By registering to the beacon, the communication between the beacon and the device becomes available.
4. Get flights info when the user's device detects the airport's beacon.
				-> If the airport has a beacon, the device knows in which airport the users are.
				-> By using the airport's APIs, the application can get the flights' info which depart from that airport.
        (* We made this application by using the API of Amsterdam airport)
				(* After the hackathon, I changed this part, because the airport's API is not open anymore.)
5. Recommends users products -> (Concept) At first, the app shows the users products based on the information from their SNS.
			-> Users decide if they want to keep the product or skip it. Whenever they decide, the application sends this information to the beacon.
			-> Using the learning machine, the beacon can recommend the users only what they would need.
			(User's traveler-type and their devices' info can be good hints for it)
	


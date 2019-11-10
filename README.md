# Cognito

Utilizes facial recognition to alert you when someone's using your laptop.

* * *

## Inspiration
As technological innovations continue to integrate technology into our lives, our personal property becomes more and more tied to the digital world. Our work, our passwords, our banking information, and more are all stored in our personal computers. Security is ever more imperative—especially so at a university campus like Princeton's, where many students feel comfortable leaving their laptops unattended.

## What it does
Our solution is Cognito—an application that alerts users if their laptops are being used by other people. After launching the app, the user simply types in their phone number, gets in position for a clear photo, and at the press of a button, Cognito will now remember the user's face. If someone other than the main user tries to use the laptop without the user being in frame, Cognito sends a text message to the user, alerting them of the malicious intruder.

## How we built it
The application was written in Java using Eclipse and Maven. We used [Webcam Capture API](https://github.com/sarxos/webcam-capture) to interface with the laptop webcam and [OpenIMAJ](http://openimaj.org/) to initially detect faces. Then, using Microsoft Cognitive Services ([Face API](https://azure.microsoft.com/en-us/services/cognitive-services/face/)), we compare all of the faces in frame to the user's stored face. Finally, if an intruder is detected, we integrated with [Twilio](https://www.twilio.com/) to send an SMS message to the user's phone.

## Challenges we ran into
Ranged from members' adversity to sleep deprivation. Also, the cold (one of our group members is from the tropics).
On a more serious note, for many of us, this was our first time participating in a Hackathon or working on integrating APIs. A significant portion of the time was spent choosing and learning to use them, and even after that, working on bug fixes in the individual components took up a substantial chunk of time.

## Accomplishments that we're proud of
Most of us did not have any experience working with APIs prior, much less under the time constraint of a Hackathon. Thus, the feeling of seeing the final working product that we had worked so hard to develop functioning as intended was pretty satisfying.

## What we learned
We did not have significant project experience outside of class assignments, and so we all learned a lot about APIs, bug fixing, and the development process in general.

## What's next for Cognito
Things we'd like to do:
- Hold a _database_ of authorized "friends" that won't trigger a warning (in addition to just the core user).
- Provide more choice regarding actions triggered by detection of an unauthorized user (e.g. sending a photo of the malicious user to the main user, disabling mouse & keyboard, turning off screen, system shutdown altogether).
- Develop a clean and more user-friendly UI.

### Note
Be sure to set the user environment variables `AZURE_SUBSCRIPTION_KEY`, `TWILIO_ACCOUNT_ID`, and `TWILIO_AUTH_TOKEN`, which correspond to your Microsoft Azure subscription key, your Twilio account id, and your Twilio authentication token. This allows you to link up your Azure and Twilio accounts with Cognito and allows for full functionality.

[Link to devpost](https://devpost.com/software/cognito)

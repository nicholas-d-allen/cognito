# Cognito

Utilizes facial recognition to alert you when someone's using your laptop.

* * *

## Inspiration
As technological innovations continue to integrate technology into our lives, our personal property becomes ever more digitized. Our work, our passwords, our banking information, etc. are all stored in our personal computers. Security is ever more imperative—especially so at university campuses like Princeton's, where many students feel comfortable leaving their laptop unattended.

## What it does
Our solution is Cognito—an application that alerts users if their laptops are being used by other people. After launching the app, the user simply types in their phone number, gets in position for a clear photo, and at the press of a button, Cognito will now the user's face. If someone other than the main user tries to use the laptop with the user being in fame, Cognito sends a text message to the main user, alerting him or her of the malicious user.

## How we built it
The application was written in Java using Eclipse and Maven. We used [Webcam Capture API](https://github.com/sarxos/webcam-capture) to access the laptop webcam and [OpenIMAJ](http://openimaj.org/) to initially detect faces. Then, using Microsoft Cognitive Services ([Face API](https://azure.microsoft.com/en-us/services/cognitive-services/face/)), we compare all of the faces in frame to the user's stored face. Finally, if an intruder is detected, we integrated with [Twilio](https://www.twilio.com/) to send an SMS message to the user's phone.

## Challenges we ran into
Some members' adversity to sleep deprivation. The cold (one of our group members is from the tropics).
On a more serious note, this was many of our first times participating in a Hackathon or working on integrating APIs. A significant portion of the time was spent choosing and learning to use the APIs. Even after that, bug fixing on the individual components took a substantial chunk of time.

## Accomplishments that we're proud of
Most of us did not have any experience working with integrating APIs, much less under the time constraint of a Hackathon. Thus, seeing a final working product that we had worked so hard to develop functioning as intended was pretty satisfying.

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

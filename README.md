<a href="https://github.com/BulatMukhutdinov/MeChat/blob/master/MeChat.apk?raw=true" download>Click to download the apk</a>
# MeChat
Don't send too big images (>1 MB) cause it would make recycler lagging. In ideal world image resizing should be perormed on backend side. Or I could use Firebase Functions API to reduce size but it is completely different story

# Task description
Need to make one-way chat. There is no interlocutor and you can talk to yourself - after all, it is always useful to talk to an intelligent person.

Each bubble in the chat can be of one of the types:
Just text
Picture
Geolocation (position from where the message was sent)
The server needs to be made too. For example, through Firebase or up to you. In fact, there you need to implement only 2 requests (receiving all messages and creating a new one)

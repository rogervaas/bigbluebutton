package org.bigbluebutton.core

import scala.actors.Actor
import scala.actors.Actor._
import org.bigbluebutton.core.apps.poll.PollApp
import org.bigbluebutton.core.apps.poll.Poll
import org.bigbluebutton.core.apps.poll.PollApp
import org.bigbluebutton.core.apps.users.UsersApp
import org.bigbluebutton.core.api.InMessage
import org.bigbluebutton.core.api.MessageOutGateway
import org.bigbluebutton.core.apps.presentation.PresentationApp
import org.bigbluebutton.core.apps.layout.LayoutApp
import org.bigbluebutton.core.apps.chat.ChatApp
import org.bigbluebutton.core.apps.whiteboard.WhiteboardApp
import org.bigbluebutton.core.apps.voice.VoiceApp
import org.bigbluebutton.core.apps.poll.messages._
import org.bigbluebutton.core.api.Presenter
  
case object StopMeetingActor

case class LockSettings(allowModeratorLocking: Boolean, disableCam: Boolean, 
                        disableMic: Boolean, disablePrivateChat: Boolean, 
                        disablePublicChat: Boolean)
                        
class MeetingActor(val meetingID: String, val recorded: Boolean, 
                   val voiceBridge: String, outGW: MessageOutGateway) 
                   extends Actor
{  
  
  var lockSettings = new LockSettings(true, true, true, true, true)
  var recordingStatus = false;
  
  val usersApp = new UsersApp(meetingID, recorded, voiceBridge, outGW)
  val presentationApp = new PresentationApp(meetingID, recorded, outGW, usersApp)
  val pollApp = new PollApp(meetingID, recorded, outGW, usersApp)
  val layoutApp = new LayoutApp(meetingID, recorded, outGW)
  val chatApp = new ChatApp(meetingID, recorded, outGW)
  val whiteboardApp = new WhiteboardApp(meetingID, recorded, outGW)
  val voiceApp = new VoiceApp(meetingID, recorded, outGW)
  
  	def act() = {
	  loop {
	    react {
	      case msg: InMessage => {
	        usersApp.handleMessage(msg)
	        presentationApp.handleMessage(msg)
	        pollApp.handleMessage(msg)
	        layoutApp.handleMessage(msg)
	        chatApp.handleMessage(msg)
	        whiteboardApp.handleMessage(msg)
	        voiceApp.handleMessage(msg)
	      }
	      case StopMeetingActor => exit
	    }
	  }
  	}
  	
}
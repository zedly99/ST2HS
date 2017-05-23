/**
 *  HS Switch
 *
 *  Copyright 2016 Steve Zed
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
 
import groovy.json.JsonBuilder

metadata {
	definition (name: "ST 2 HS", namespace: "zedly99", author: "Steve Zed") {
		capability "Switch"
	}


	simulator {
		// TODO: define status and reply messages here
        
	}

	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.switches.light.off", backgroundColor: "#ffffff", nextState: "on"
			state "on", label: 'On', action: "switch.off", icon: "st.switches.light.on", backgroundColor: "#79b821", nextState: "off"
		}
		main "button"
		details "button"
	}
}


// handle commands
def on() {
	log.debug "Executing 'on'"
    def headers = [:] 
	headers.get("HOST", "192.168.1.3:80")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def url = "192.168.1.3:80"
    def path = "/JSON?request=controldevicebyvalue&ref=8&value=100"
    
  	  def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "192.168.1.3:80",
            path: "/JSON?request=controldevicebyvalue&ref=8&value=100",
            headers: headers)
    
    return hubAction

}

 

def off() {
	log.debug "Executing 'off'"
    def headers = [:] 
	headers.get("HOST", "192.168.1.3:80")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def url = "192.168.1.3:80"
    def path = "/JSON?request=controldevicebyvalue&ref=8&value=0"
    

  	  def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "192.168.1.3:80",
            path: "/JSON?request=controldevicebyvalue&ref=8&value=0",
            headers: headers)
            
    return hubAction

}


// parse attributes into events
def parse(description) {

    def msg = parseLanMessage(description)
//	log.debug "Parsing '${msg}'"
    def headersAsString = msg.header // => headers as a string
    def headerMap = msg.headers      // => headers as a Map
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response
    def json = msg.json              // => any JSON included in response body, as a data structure of lists and maps
    def xml = msg.xml                // => any XML included in response body, as a document tree structure
    def data = msg.data              // => either JSON or XML in response body (whichever is specified by content-type header in response)
	log.debug json 
    
	// TODO: handle 'switch' attribute
	// TODO: handle 'switch' attribute
	// TODO: handle 'level' attribute

}

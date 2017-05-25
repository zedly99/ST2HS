/**
 *  Copyright 2015 SmartThings
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
metadata {
	// Automatically generated. Make future change here.
	definition (name: "Simulated Thermostat-SZ", namespace: "stevezed99", author: "Steve") {
		capability "Thermostat"
		command "off"
        command "heat"
        command "cool"
		command "heatUp"
		command "heatDown"
		command "coolUp"
		command "coolDown"
		command "setTemperature", ["number"]
        command "setThermostatMode", ["string"]
        command "setThermostatFanMode", ["string"]
        command "setThermostatOperatingState", ["string"]
	}

tiles (scale:2) {

	 		standardTile("mode", "device.thermostatMode", width: 2, height: 2, decoration: "flat") { 
 				state "off", label:'Mode', action:"heat", icon:"st.thermostat.heating-cooling-off" 
				state "heat", label:'Mode', action:"cool", icon:"st.thermostat.heat" 
 				state "cool", label:'Mode', action:"off", icon:"st.thermostat.cool" 
			}
            
            valueTile("temperature","device.temperature", width: 2, height: 2) { 
 				state("temperature", label:'${currentValue}°', unit:'F', 
 					backgroundColors:[ 
 						[value: 31, color: "#153591"], 
 						[value: 44, color: "#1e9cbb"], 
 						[value: 59, color: "#90d2a7"], 
 						[value: 74, color: "#44b621"], 
 						[value: 84, color: "#f1d801"], 
 						[value: 95, color: "#d04e00"], 
 						[value: 96, color: "#bc2323"] 
 					] 
 				) 
		 	} 
            
            standardTile("operatingState","device.thermostatOperatingState", width: 2, height: 2) {
				state "idle", label:'Operating',backgroundColor:"#ffffff",icon:"st.thermostat.heating-cooling-off"
				state "heating", label:'Operating',backgroundColor:"##ffa81e",icon:"st.thermostat.heating"
				state "cooling", label:'Operating',backgroundColor:"#269bd2",icon:"st.thermostat.cooling"
			}

			valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat") {
				state "heat", label:'${currentValue} heat', unit: "F", backgroundColor:"#ffffff"
			}
            
			standardTile("heatDown", "device.temperature", inactiveLabel: false, decoration: "flat") {
				state "default", label:'', action:"heatDown",icon:"st.thermostat.thermostat-down"
			}
            
			standardTile("heatUp", "device.temperature", inactiveLabel: false, decoration: "flat") {
				state "default", label:'', action:"heatUp", icon:"st.thermostat.thermostat-up"
			}

			valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false, decoration: "flat") {
				state "cool", label:'${currentValue} cool', unit:"F", backgroundColor:"#ffffff"
			}
            
			standardTile("coolDown", "device.temperature", inactiveLabel: false, decoration: "flat") {
				state "default", label:'', action:"coolDown",icon:"st.thermostat.thermostat-down"
			}
		
        	standardTile("coolUp", "device.temperature", inactiveLabel: false, decoration: "flat") {
				state "default", label:'', action:"coolUp", icon:"st.thermostat.thermostat-up"
			}

			standardTile("fanMode", "device.thermostatFanMode", inactiveLabel: false, decoration: "flat") {
				state "fanAuto", label:'${name}', action:"thermostat.fanOn", backgroundColor:"#ffffff"
				state "fanOn", label:'${name}', action:"thermostat.fanAuto", backgroundColor:"#ffffff"
			}
         

      }

		main ('temperature')
		details(["mode","temperature","operatingState","heatDown","heatingSetpoint","heatUp","coolDown","coolingSetpoint", "coolUp","fanMode"])
}	


def installed() {
	sendEvent(name: "temperature", value: 72, unit: "F")
	sendEvent(name: "heatingSetpoint", value: 70, unit: "F")
	sendEvent(name: "thermostatSetpoint", value: 70, unit: "F")
	sendEvent(name: "coolingSetpoint", value: 76, unit: "F")
	sendEvent(name: "thermostatMode", value: "off")
	sendEvent(name: "thermostatFanMode", value: "fanAuto")
	sendEvent(name: "thermostatOperatingState", value: "idle")
}

def parse(String description) {
}

def evaluate(temp, heatingSetpoint, coolingSetpoint) {
	log.debug "evaluate($temp, $heatingSetpoint, $coolingSetpoint)"
	def threshold = 1.0
	def current = device.currentValue("thermostatOperatingState")
	def mode = device.currentValue("thermostatMode")

	def heating = false
	def cooling = false
	def idle = false
	if (mode in ["heat","emergency heat","auto"]) {
		if (heatingSetpoint - temp >= threshold) {
			heating = true
			sendEvent(name: "thermostatOperatingState", value: "heating")
		}
		else if (temp - heatingSetpoint >= threshold) {
			idle = true
		}
		sendEvent(name: "thermostatSetpoint", value: heatingSetpoint)
	}
	if (mode in ["cool","auto"]) {
		if (temp - coolingSetpoint >= threshold) {
			cooling = true
			sendEvent(name: "thermostatOperatingState", value: "cooling")
		}
		else if (coolingSetpoint - temp >= threshold && !heating) {
			idle = true
		}
		sendEvent(name: "thermostatSetpoint", value: coolingSetpoint)
	}
	else {
		sendEvent(name: "thermostatSetpoint", value: heatingSetpoint)
	}
	if (idle && !heating && !cooling) {
		sendEvent(name: "thermostatOperatingState", value: "idle")
	}
}

def setHeatingSetpoint(int degreesF) {
	log.debug "setHeatingSetpoint($degreesF)"
	sendEvent(name: "heatingSetpoint", value: degreesF)
	evaluate(device.currentValue("temperature"), degreesF, device.currentValue("coolingSetpoint"))
}

def setCoolingSetpoint(int degreesF) {
	log.debug "setCoolingSetpoint($degreesF)"
	sendEvent(name: "coolingSetpoint", value: degreesF)
	evaluate(device.currentValue("temperature"), device.currentValue("heatingSetpoint"), degreesF)
}

def setThermostatMode(String value) {
	sendEvent(name: "thermostatMode", value: value)
	evaluate(device.currentValue("temperature"), device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def setThermostatFanMode(String value) {
	sendEvent(name: "thermostatFanMode", value: value)
}

def setThermostatOperatingState(String value) {
	sendEvent(name: "thermostatOperatingState", value: value)
}
def off() {
	sendEvent(name: "thermostatMode", value: "off")
}

def heat() {
	sendEvent(name: "thermostatMode", value: "heat")

}

def auto() {
	sendEvent(name: "thermostatMode", value: "auto")
	evaluate(device.currentValue("temperature"), device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def emergencyHeat() {
	sendEvent(name: "thermostatMode", value: "emergency heat")
}

def cool() {
	sendEvent(name: "thermostatMode", value: "cool")
}

def fanOn() {
	sendEvent(name: "thermostatFanMode", value: "fanOn")
}

def fanAuto() {
	sendEvent(name: "thermostatFanMode", value: "fanAuto")
}

def fanCirculate() {
	sendEvent(name: "thermostatFanMode", value: "fanCirculate")
}

def poll() {
	null
}

def tempUp() {
	def ts = device.currentState("temperature")
	def value = ts ? ts.integerValue + 1 : 72
	sendEvent(name:"temperature", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def tempDown() {
	def ts = device.currentState("temperature")
	def value = ts ? ts.integerValue - 1 : 72
	sendEvent(name:"temperature", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def setTemperature(value) {
	def ts = device.currentState("temperature")
	sendEvent(name:"temperature", value: value)
	evaluate(value, device.currentValue("heatingSetpoint"), device.currentValue("coolingSetpoint"))
}

def heatUp() {
	log.debug "heat up"
	def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue + 1 : 68
	sendEvent(name:"heatingSetpoint", value: value)
	evaluate(device.currentValue("temperature"), value, device.currentValue("coolingSetpoint"))
}

def heatDown() {
	def ts = device.currentState("heatingSetpoint")
	def value = ts ? ts.integerValue - 1 : 68
	sendEvent(name:"heatingSetpoint", value: value)
	evaluate(device.currentValue("temperature"), value, device.currentValue("coolingSetpoint"))
}


def coolUp() {
	def ts = device.currentState("coolingSetpoint")
	def value = ts ? ts.integerValue + 1 : 76
	sendEvent(name:"coolingSetpoint", value: value)
	evaluate(device.currentValue("temperature"), device.currentValue("heatingSetpoint"), value)
}

def coolDown() {
	def ts = device.currentState("coolingSetpoint")
	def value = ts ? ts.integerValue - 1 : 76
	sendEvent(name:"coolingSetpoint", value: value)
	evaluate(device.currentValue("temperature"), device.currentValue("heatingSetpoint"), value)
}

def switchMode() { 
	log.debug "switchMode"
	def currentMode = device.currentState("thermostatMode")?.value 
 	def lastTriedMode = getDataByName("lastTriedMode") ?: currentMode ?: "off" 
 	def supportedModes = getDataByName("supportedModes") 
 	def modeOrder = modes() 
 	def next = { modeOrder[modeOrder.indexOf(it) + 1] ?: modeOrder[0] } 
 	def nextMode = next(lastTriedMode) 
 	if (supportedModes?.contains(currentMode)) { 
 		while (!supportedModes.contains(nextMode) && nextMode != "off") { 
 			nextMode = next(nextMode) 
 		} 
 	} 
     log.debug "Switching to mode: ${nextMode}" 
 	switchToMode(nextMode) 
 } 

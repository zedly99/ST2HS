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
	definition (name: "HS Thermostat", namespace: "zedly99", author: "Steve Zed") {
		capability "Thermostat"
		command "tempUp"
		command "tempDown"
		command "heatUp"
		command "heatDown"
		command "coolUp"
		command "coolDown"
        command "setThermostatMode", ["string"]
		command "setCoolingSetpoint",["number"]
        command "setHeatingSetpoint",["number"]
        
	}

	preferences {
    	input name: "HSIPAddress", type: "text", title: "HS IP Address", description: "IP Address of Homeseer Server", required: true
        input name: "HSPort", type: "text", title: "HS port", description: "Port of Homeseer Server", required: true
        input name: "coolingSetpointDevice", type: "number", title: "Cooling Setpoint Ref Number", description: "Reference Number of Cooling Setpoint Device", required: true
        input name: "heatingSetpointDevice", type: "number", title: "Heating Setpoint Ref Number", description: "Reference Number of Heating Setpoint Device", required: true        
   		input name: "currentTemperatureDevice", type: "number", title: "Current Temperature Ref Number", description: "Reference Number of Current Temperature Device", required: true 
    	input name: "thermostatModeDevice", type: "number", title: "Thermostat Mode Ref Number", description: "Reference Number of Thermostat Mode Device", required: true
		input name: "currentStateDevice", type: "number", title: "Current State Ref Number", description: "Reference Number of Current State Device", required: true

}
	tiles(scale: 2) {
		multiAttributeTile(name:"thermostatMulti", type:"thermostat", width:6, height:4) {
			tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
				attributeState("default", label:'${currentValue}', unit:"dF")
			}
			tileAttribute("device.temperature", key: "VALUE_CONTROL") {
				attributeState("VALUE_UP", action: "tempUp")
				attributeState("VALUE_DOWN", action: "tempDown")
			}
			tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
				attributeState("default", label:'${currentValue}%', unit:"%")
			}
			tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
				attributeState("idle", backgroundColor:"#44b621")
				attributeState("heating", backgroundColor:"#ea5462")
				attributeState("cooling", backgroundColor:"#269bd2")
			}
			tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
				attributeState("off", label:'${name}')
				attributeState("heat", label:'${name}')
				attributeState("cool", label:'${name}')

			}
			tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
				attributeState("default", label:'${currentValue}', unit:"dF")
			}
			tileAttribute("device.coolingSetpoint", key: "COOLING_SETPOINT") {
				attributeState("default", label:'${currentValue}', unit:"dF")
			}
		}

		valueTile("temperature", "device.temperature", width: 2, height: 2) {
			state("temperature", label:'${currentValue}', unit:"dF",
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
		standardTile("tempDown", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'down', action:"tempDown"
		}
		standardTile("tempUp", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'up', action:"tempUp"
		}

		valueTile("heatingSetpoint", "device.heatingSetpoint", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "heat", label:'${currentValue} heat', unit: "F", backgroundColor:"#ffffff"
		}
		standardTile("heatDown", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'down', action:"heatDown"
		}
		standardTile("heatUp", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'up', action:"heatUp"
		}

		valueTile("coolingSetpoint", "device.coolingSetpoint", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "cool", label:'${currentValue} cool', unit:"F", backgroundColor:"#ffffff"
		}
		standardTile("coolDown", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'down', action:"coolDown"
		}
		standardTile("coolUp", "device.temperature", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:'up', action:"coolUp"
		}

		standardTile("mode", "device.thermostatMode", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "off", label:'${name}', action:"thermostat.heat", backgroundColor:"#ffffff"
			state "heat", label:'${name}', action:"thermostat.cool", backgroundColor:"#ffa81e"
			state "cool", label:'${name}', action:"thermostat.auto", backgroundColor:"#269bd2"
		}
		standardTile("fanMode", "device.thermostatFanMode", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
			state "fanAuto", label:'${name}', action:"thermostat.fanOn", backgroundColor:"#ffffff"
			state "fanOn", label:'${name}', action:"thermostat.fanAuto", backgroundColor:"#ffffff"

		}
		standardTile("operatingState", "device.thermostatOperatingState", width: 2, height: 2) {
			state "idle", label:'${name}', backgroundColor:"#ffffff"
			state "heating", label:'${name}', backgroundColor:"#ffa81e"
			state "cooling", label:'${name}', backgroundColor:"#269bd2"
		}

		main("thermostatMulti")
		details([
			"temperature","tempDown","tempUp",
			"mode", "fanMode", "operatingState",
			"heatingSetpoint", "heatDown", "heatUp",
			"coolingSetpoint", "coolDown", "coolUp",
		])
	}
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
    def msg = parseLanMessage(description)
    def headersAsString = msg.header // => headers as a string
    def headerMap = msg.headers      // => headers as a Map
    def body = msg.body              // => request body as a string
    def status = msg.status          // => http status code of the response
    def json = msg.json              // => any JSON included in response body, as a data structure of lists and maps
    def xml = msg.xml                // => any XML included in response body, as a document tree structure
    def data = msg.data              // => either JSON or XML in response body (whichever is specified by content-type header in response)

//Figure out which device code we have and which event to fire
	log.debug json.Devices.ref
    log.debug json.Devices.value

	switch (jason.Devices.ref){
		case coolingSetpointDevice: sendEvent(name:"coolingSetpoint", value: json.Devices.value)
		case heatingSetpointDevice: sendEvent(name:"heatingSetpoint", value: json.Devices.value)
        case currentTemperatureDevice: sendEvent(name:"temperature", value: json.Devices.value)
//      case thermostatModeDevice: 
				switch (json.Devices.value) {
                	case 0: sendEvent(name: "thermostatMode", value: "off")
                    case 1: sendEvent(name: "thermostatMode", value: "heating")
                    case 2: sendEvent(name: "thermostatMode", value: "cooling")
                }
        case currentStateDevice:
				switch (json.Devices.value) {
                	case 0: sendEvent(name: "thermostatOperatingState", value: "idle")
                    case 1: sendEvent(name: "thermostatOperatingState", value: "heat")
                    case 2: sendEvent(name: "thermostatOperatingState", value: "cool")
                }
	}
}

def evaluate(temp, heatingSetpoint, coolingSetpoint) {
	log.debug "evaluate($temp, $heatingSetpoint, $coolingSetpoint"
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

def setHeatingSetpoint(Double degreesF) {
	log.debug "setHeatingSetpoint($degreesF)"
        def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$heatingSetpointDevice&value=$degreesF",
            headers: headers)
    log.debug hubAction
	sendEvent(name:"HeatingSetpoint", value: degreesF)
    return hubAction


}

def setCoolingSetpoint(Double degreesF) {
	log.debug "setCoolingSetpoint($degreesF)"
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$coolingSetpointDevice&value=$degreesF",
            headers: headers)
    log.debug hubAction

    return hubAction
}

def setThermostatMode(String modeString) {
	log.debug "setThermostatMode($modeString)"
    def modeValue = 0
    switch (modeString) {
    	case "off": modeValue = 0; break;
        case "heat": modeValue = 1; break;
        case "cool": modeValue = 2; break;
    }
    log.debug "setThermostatModeValue($modeValue)"
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$thermostatModeDevice&value=$modeValue",
            headers: headers)
    log.debug hubAction
	sendEvent(name: "thermostatMode", value: modeString)
    return hubAction
	
	
}

def setThermostatFanMode(String value) {
	sendEvent(name: "thermostatFanMode", value: value)
}


def tempUp() {
	log.debug "Executing 'temp up'"
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
	def value = ts.integerValue + 1
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$heatingSetpointDevic&value=$value",
            headers: headers)
    	log.debug hubAction
		sendEvent(name:"heatingSetpoint", value: value)
    return hubAction
}

def heatDown() {
	log.debug "heat down"
	def ts = device.currentState("heatingSetpoint")
	def value = ts.integerValue - 1
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$heatingSetpointDevice&value=$value",
            headers: headers)
    log.debug hubAction
	sendEvent(name:"heatingSetpoint", value: value)
    return hubAction
}


def coolUp() {
	log.debug "cool up"
	def ts = device.currentState("coolingSetpoint")
	def value = ts.integerValue + 1
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$coolingSetpointDevice&value=$value",
            headers: headers)
    	log.debug hubAction
		sendEvent(name:"coolingSetpoint", value: value)
        return hubAction
}

def coolDown() {
	log.debug "cool down"
	def ts = device.currentState("coolingSetpoint")
	def value = ts.integerValue - 1
    def headers = [:] 
	headers.get("HOST", "$HSIPAddress:$HSPort")
	headers.get("Content-Type", "application/json")
	def method = "GET"
    def hubAction = new physicalgraph.device.HubAction(
    	    method: method,
     	  	url: "$HSIPAddress:$HSPort",
            path: "/JSON?request=controldevicebyvalue&ref=$coolingSetpointDevice&value=$value",
            headers: headers)
    log.debug hubAction
	sendEvent(name:"coolingSetpoint", value: value)
    return hubAction
}

/**
 * Created by apple on 16-10-20.
 */
(function(){


    //var Events_base  = null;

    /**
     *
     * @param _EVENTS  该对象支持的事件map
     * @returns {*}
     * @constructor
     */
    var Base_Custom_Events = function(_EVENTS){

        if(this == window) throw "Base_Custom_Events must be new..";

        var EVENTS = _EVENTS;


        //if(Events_base) return Events_base;

        var Events_base = function(){
            if(!this.Events_List) this.Events_List = {};
            /**
             * 监听事件
             * @param _eventName
             * @param _callBack
             */
            //if(!this.__proto__){//IE
                if(!Object.getPrototypeOf(this).onEvents)
                    Object.getPrototypeOf(this).onEvents = function(_eventName,_callBack){
                        if(EVENTS && EVENTS[_eventName] == undefined) throw "Do not Support Events: "+_eventName;
                        if(!_callBack instanceof Function) throw "Do not Support Events: "+_eventName;

                        if(!this.Events_List[_eventName]) this.Events_List[_eventName] = [];
                        this.Events_List[_eventName].push(_callBack);
                        return  this.Events_List[_eventName].length-1;
                    };
                /**
                 * 触发事件
                 * @param _eventName
                 * @param _eventObj
                 */
                if(!Object.getPrototypeOf(this).fireEvents)
                    Object.getPrototypeOf(this).fireEvents = function(_eventName,_eventObj){
                        if(!this.Events_List[_eventName]) return;
                        var eventList = this.Events_List[_eventName];
                        for(var i=0;i<eventList.length;i++){
                            if(!(eventList[i] instanceof Function)) continue;
                            eventList[i].call(this,_eventObj);
                        }
                    };

                if(!Object.getPrototypeOf(this).offEvents)
                    Object.getPrototypeOf(this).offEvents = function(_eventName,_index){
                        if(!this.Events_List[_eventName]) return;
                        var eventList = this.Events_List[_eventName];
                        if(eventList[_index])eventList[_index] = null;
                    };

                if(!Object.getPrototypeOf(this).offAll)
                    Object.getPrototypeOf(this).offAll = function(_eventName){
                        if(!this.Events_List[_eventName]) return;
                        this.Events_List[_eventName] = null;

                    };
        };

        return Events_base;
    };

    if(!window.np) window.np = {};
    if(!np.Utils) np.Utils = {};

    np.Utils.Event_Base = Base_Custom_Events;

})();
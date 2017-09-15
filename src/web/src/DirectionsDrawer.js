import React, { Component } from 'react';
import { InputGroupButton, InputGroupAddon, InputGroup, Input, Button, ButtonDropdown, DropdownItem, DropdownMenu } from 'reactstrap';
import { Icon } from 'react-fa'

class DirectionsDrawer extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onZoomRoom = this._event_onZoomRoom.bind(this);
        this._event_onQuery = this._event_onQuery.bind(this);
        this._event_onPostQuery = this._event_onPostQuery.bind(this);
        this._event_onSearchKeyDown = this._event_onSearchKeyDown.bind(this);
        this._event_onGetDirections = this._event_onGetDirections.bind(this);
        this._render_autoDrop = this._render_autoDrop.bind(this);
        window.addEventListener("click", () =>
        {
            this.setState({
                DropDown: []
            });
        });
    }
    _event_onZoomRoom(event)
    {
        let txt = document.getElementById("searchbar2").value;
        window.dispatchEvent(new CustomEvent("_event_onZoomRoom", { detail: { text: txt } }));
        this.setState({
            DropDown: []
        });
    }
    _event_onQuery(event)
    {
        let txt = document.getElementById("searchbar2").value;
        window.dispatchEvent(new CustomEvent("_event_onQuery", { detail: { query: txt, callback: this._event_onPostQuery } }));
    }
    _event_onPostQuery(event)
    {
        this.setState({
            DropDown: event
        });
    }
    _event_onSearchKeyDown(event)
    {
        this._event_onQuery();
        if (event.keyCode === 13)
        {
            this._event_onZoomRoom();
            event.target.blur();
        }
        else if (event.keyCode === 40) //Down
        {
            let id = document.getElementById("dropdown2:0");
            if (id)
            {
                id.focus();               
                this.oldText = document.getElementById("searchbar2").value;
                document.getElementById("searchbar2").value = this.state.DropDown[0].Name;
                let container = document.getElementById("dropdown2:container1");
                container.scrollTop = 0;
                container = document.getElementById("dropdown2:container2");
                container.scrollTop = 0;
            }
        }
    }
    _event_onGetDirections(event)
    {
        if (document.getElementById("searchbar1").value !== document.getElementById("searchbar2").value)
        {
            window.dispatchEvent(new CustomEvent("_event_onGetDirections", { detail: { loc1: document.getElementById("searchbar1").value, loc2: document.getElementById("searchbar2").value } }));
        }
    }    
    componentWillMount()
    {
        this.setState({
            DropDown: []
        });
    }
    _render_autoDrop()
    {
        if (this.state.DropDown.length > 0)
        {
            let itms = [];
            for (let i = 0; i < this.state.DropDown.length; i++)
            {
                itms.push(<DropdownItem id={"dropdown2:" + i} onClick={() =>
                {
                    document.getElementById("searchbar2").value = this.state.DropDown[i].Name;
                    this._event_onZoomRoom();
                }} onKeyDown={(event) =>
                {
                    if (event.keyCode === 40)
                    {
                        let id = document.getElementById("dropdown2:" + (i + 1));
                        if (id)
                        {
                            id.focus();       
                            document.getElementById("searchbar2").value = this.state.DropDown[i + 1].Name;
                        }
                    }
                    else if (event.keyCode === 38)
                    {
                        let id = document.getElementById("dropdown2:" + (i - 1));
                        if (id)
                        {
                            id.focus();                
                            document.getElementById("searchbar2").value = this.state.DropDown[i - 1].Name;
                        }
                        else
                        {
                            let txt = document.getElementById("searchbar2");
                            txt.focus();
                            document.getElementById("searchbar2").value = this.oldText;
                        }
                    }
                }}>{ this.state.DropDown[i].Name }</DropdownItem >)
            }
            return (                
                <ButtonDropdown onKeyDown={(e) =>
                {
                    if ([32, 37, 38, 39, 40].indexOf(e.keyCode) > -1)
                    {
                        e.preventDefault();
                    }
                }} id="dropdown2:container1" style={{ position: "fixed", zIndex: 10, left: 60 }} isOpen={true}>
                    <DropdownMenu onKeyDown={(e) =>
                    {
                        if ([32, 37, 38, 39, 40].indexOf(e.keyCode) > -1)
                        {
                            e.preventDefault();
                        }
                    }} id="dropdown2:container2" style={{ margin: 0, width: 282, maxHeight: 200, overflowX: "hidden", borderTop: "none", borderTopLeftRadius: 0, borderTopRightRadius: 0 }}>
                        {itms}
                    </DropdownMenu>
                </ButtonDropdown>
            );
        }
        else
        {
            return null;
        }
    }
    render()
    {
        return (
            <div style={{
                backgroundColor: "white",
                marginTop: 5,
                borderBottomLeftRadius: 10,
                borderBottomRightRadius: 10,
                boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)"
            }}>
                <InputGroup style={{ border: "none" }}>
                    <InputGroupAddon style={{ border: "none", paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, borderBottomLeftRadius: 10, borderTopLeftRadius: 0 }}>
                        End
                    </InputGroupAddon>
                    <Input id="searchbar2" style={{ border: "none" }} placeholder="Choose Destination..." onClick={this._event_onQuery} onChange={this._event_onQuery} onKeyDown={this._event_onSearchKeyDown} />
                    <InputGroupButton>
                        <Button outline color="primary" style={{ padding: 0, width: 50, border: "none" }} onClick={this._event_onZoomRoom}>
                            <Icon name="search" />
                        </Button>
                    </InputGroupButton>
                    <InputGroupAddon style={{ padding: 0, width: 2, marginTop: 10, marginBottom: 10 }}></InputGroupAddon>
                    <InputGroupButton>
                        <Button outline color="warning" style={{ padding: 0, width: 50, border: "none", borderBottomRightRadius: 10, borderTopRightRadius: 0 }} onClick={this._event_onGetDirections}>
                            <Icon name="angle-right" />
                        </Button>
                    </InputGroupButton>
                </InputGroup>
                {this._render_autoDrop()}
            </div>
        );
    }
}
export default DirectionsDrawer;

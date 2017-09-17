import React, { Component } from 'react';
import { Collapse, InputGroupButton, InputGroupAddon, InputGroup, Input, Button, ButtonDropdown, DropdownMenu, DropdownItem } from 'reactstrap';
import { Icon } from 'react-fa';
import NavigationDrawer from './NavigationDrawer';
import DirectionsDrawer from './DirectionsDrawer';
import InformationDrawer from './InformationDrawer';

class Navigation extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onMenuClick = this._event_onMenuClick.bind(this);
        this._event_onDirectionsClick = this._event_onDirectionsClick.bind(this);
        this._event_onZoomRoom = this._event_onZoomRoom.bind(this);
        this._event_onQuery = this._event_onQuery.bind(this);
        this._event_onPostQuery = this._event_onPostQuery.bind(this);
        this._event_onSearchKeyDown = this._event_onSearchKeyDown.bind(this);
        this._event_onPostSetDirection = this._event_onPostSetDirection.bind(this);
        this._event_onShowInfo = this._event_onShowInfo.bind(this);
        this._event_onHideInfo = this._event_onHideInfo.bind(this);

        this._render_BorderRadius = this._render_BorderRadius.bind(this);
        this._render_leftNavigation = this._render_leftNavigation.bind(this);
        this._render_leftBorder = this._render_leftBorder.bind(this);
        this._render_autoDrop = this._render_autoDrop.bind(this);

        this.PlaceHolderRegular = "Search SBHS Maps";
        this.PlaceHolderDirections = "Choose Start..."

        this.DrawerIconClosed = "angle-down"; //bars | angle-down
        this.DrawerIconOpened = "angle-up";
        this.DirectionsIconClosed = "map-marker";
        this.DirectionsIconOpened = "angle-up";

        window.addEventListener("_event_onPostSetDirection", this._event_onPostSetDirection);
        window.addEventListener("click", () =>
        {
            this.setState({
                DropDown: []
            });
        });
        window.addEventListener("keydown", function(e) {
            // space and arrow keys
            if([32, 37, 38, 39, 40].indexOf(e.keyCode) > -1) {
                //e.preventDefault();
            }
        }, false);
        window.addEventListener("_event_onElementClick", this._event_onMenuClick);
        window.addEventListener("_event_onShowInfo", this._event_onShowInfo);
        window.addEventListener("_event_onHideInfo", this._event_onHideInfo);
    }
    _event_onMenuClick()
    {
        let dirIcon;
        if (!this.state.Drawer)
        {
            dirIcon = this.DrawerIconOpened;
        }
        else
        {
            dirIcon = this.DrawerIconClosed;
        }
        this.setState({
            Drawer: !this.state.Drawer,
            DrawerIcon: dirIcon,
            Directions: false,
            DirectionsIcon: this.DirectionsIconClosed,
            PlaceHolder: this.PlaceHolderRegular
        });
    }
    _event_onDirectionsClick()
    {
        let dirIcon;
        let pT;
        let info = this.state.ShowInfo;
        if (!this.state.Directions)
        {
            dirIcon = this.DirectionsIconOpened;
            pT = this.PlaceHolderDirections;
        }
        else
        {
            dirIcon = this.DirectionsIconClosed;
            pT = this.PlaceHolderRegular;
            info = false;
        }
        this.setState({
            Drawer: false,
            DrawerIcon: this.DrawerIconClosed,
            Directions: !this.state.Directions,
            DirectionsIcon: dirIcon,
            PlaceHolder: pT,
            Info: info
        });
    }
    _event_onPostSetDirection()
    {
        if (!this.state.Directions)
        {
            this._event_onDirectionsClick();
        }
    }
    _event_onZoomRoom(event)
    {
        let txt = document.getElementById("searchbar1").value;
        window.dispatchEvent(new CustomEvent("_event_onZoomRoom", { detail: { text: txt } }));
        this.setState({
            DropDown: []
        });
    }
    _event_onQuery(event)
    {
        let txt = document.getElementById("searchbar1").value;
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
            if (this.state.Directions)
            {
                document.getElementById("searchbar2").focus();
            }
        }
        else if (event.keyCode === 40) //Down
        {
            let id = document.getElementById("dropdown:0");
            if (id)
            {
                id.focus();
                this.oldText = document.getElementById("searchbar1").value;
                document.getElementById("searchbar1").value = this.state.DropDown[0].Name;
                let container = document.getElementById("dropdown:container1");
                container.scrollTop = 0;
                container = document.getElementById("dropdown:container2");
                container.scrollTop = 0;
            }
        }
    }
    _event_onShowInfo(event)
    {
        this.setState({
            Info: true,
            ShowInfo: true,
            dirArr: event.detail.dirArr
        });
    }
    _event_onHideInfo(event)
    {
        this.setState({
            Info: false,
            ShowInfo: false,
            dirArr: null
        });
    }
    componentWillMount()
    {
        this.setState({
            Drawer: false,
            DrawerIcon: this.DrawerIconClosed,
            Directions: false,
            DirectionsIcon: this.DirectionsIconClosed,
            BorderRadius: 0,
            PlaceHolder: this.PlaceHolderRegular,
            DropDown: [],
            Info: false,
            ShowInfo: false
        });
    }
    _render_BorderRadius()
    {
        if (this.state.Drawer || this.state.Directions)
        {
            if (this.state.BorderRadius < 10)
            {
                setTimeout(() =>
                {
                    this.setState({
                        BorderRadius: this.state.BorderRadius + 1
                    });
                }, 10);
            }
        }
        else
        {
            if (this.state.BorderRadius > 0)
            {
                setTimeout(() =>
                {
                    this.setState({
                        BorderRadius: this.state.BorderRadius - 1
                    });
                }, 10);
            }
        }
        return null;
    }
    _render_leftNavigation()
    {
        if (!this.state.Directions)
        {
            return (
                <InputGroupButton>
                    <Button outline color="primary" active={this.state.Drawer} onClick={this._event_onMenuClick} style={{ paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, border: "none", borderBottomLeftRadius: 0, borderTopLeftRadius: this.state.BorderRadius }}>
                        <Icon name={this.state.DrawerIcon} size="lg" />
                    </Button>
                </InputGroupButton>
            );
        }
        else
        {
            return (
                <InputGroupAddon style={{ paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, border: "none", borderBottomLeftRadius: 0, borderTopLeftRadius: this.state.BorderRadius }}>
                    Start
                </InputGroupAddon>
            );
        }
    }
    _render_leftBorder()
    {
        if (!this.state.Directions)
        {
            return (
                <InputGroupAddon style={{ padding: 0, width: 2, marginTop: 10, marginBottom: 10 }}></InputGroupAddon>
            );
        }
        else
        {
            return null;
        }
    }
    _render_autoDrop()
    {
        if (this.state.DropDown.length > 0)
        {
            let itms = [];
            for (let i = 0; i < this.state.DropDown.length; i++)
            {
                itms.push(<DropdownItem id={"dropdown:" + i} onClick={() =>
                {
                    document.getElementById("searchbar1").value = this.state.DropDown[i].Name;
                    this._event_onZoomRoom();
                }} onKeyDown={(event) =>
                {
                    if (event.keyCode === 40)
                    {
                        let id = document.getElementById("dropdown:" + (i + 1));
                        if (id)
                        {
                            id.focus();
                            document.getElementById("searchbar1").value = this.state.DropDown[i + 1].Name;
                        }
                    }
                    else if (event.keyCode === 38)
                    {
                        let id = document.getElementById("dropdown:" + (i - 1));
                        if (id)
                        {
                            id.focus();
                            document.getElementById("searchbar1").value = this.state.DropDown[i - 1].Name;
                        }
                        else
                        {
                            let txt = document.getElementById("searchbar1");
                            txt.focus();
                            document.getElementById("searchbar1").value = this.oldText;
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
                }} id="dropdown:container1" style={{ position: "fixed", zIndex: 10, left: 60 }} isOpen={true}>
                    <DropdownMenu onKeyDown={(e) =>
                    {
                        if ([32, 37, 38, 39, 40].indexOf(e.keyCode) > -1)
                        {
                            e.preventDefault();
                        }
                    }} id="dropdown:container2" style={{ margin: 0, width: 282, maxHeight: 200, overflowX: "hidden", borderTop: "none", borderTopLeftRadius: 0, borderTopRightRadius: 0 }}>
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
                backgroundColor: "rgba(0, 0, 0, 0)",
                position: "fixed",
                padding: 10,
                width: 400
            }}>
                <div style={{
                    backgroundColor: "white",
                    borderTopLeftRadius: this.state.BorderRadius,
                    borderTopRightRadius: this.state.BorderRadius,
                    boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)"
                }}>
                    <InputGroup style={{ border: "none" }}>
                        {this._render_leftNavigation()}
                        {this._render_leftBorder()}
                        <Input id="searchbar1" style={{ border: "none" }} placeholder={this.state.PlaceHolder} onClick={this._event_onQuery} onChange={this._event_onQuery} onKeyDown={this._event_onSearchKeyDown} />
                        <InputGroupButton>
                            <Button outline color="primary" style={{ padding: 0, width: 50, border: "none" }} onClick={this._event_onZoomRoom}>
                                <Icon name="search" />
                            </Button>
                        </InputGroupButton>
                        <InputGroupAddon style={{ padding: 0, width: 2, marginTop: 10, marginBottom: 10 }}></InputGroupAddon>
                        <InputGroupButton>
                            <Button outline color="success" active={this.state.Directions} onClick={this._event_onDirectionsClick} style={{ padding: 0, width: 50, border: "none", borderBottomRightRadius: 0, borderTopRightRadius: this.state.BorderRadius }}>
                                <Icon name={this.state.DirectionsIcon} />
                            </Button>
                        </InputGroupButton>
                    </InputGroup>
                </div>
                {this._render_autoDrop()}
                <Collapse isOpen={this.state.Drawer} navbar>
                    <NavigationDrawer />
                </Collapse>
                <Collapse isOpen={this.state.Info} navbar>
                    <InformationDrawer dirArr={this.state.dirArr}/>
                </Collapse>
                <Collapse isOpen={this.state.Directions} navbar>
                    <DirectionsDrawer />
                </Collapse>
                {this._render_BorderRadius()}
            </div>
        );
    }
}
export default Navigation;

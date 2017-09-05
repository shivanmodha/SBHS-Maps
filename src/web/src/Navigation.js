import React, { Component } from 'react';
import { Collapse, InputGroupButton, InputGroupAddon, InputGroup, Input, Button } from 'reactstrap';
import { Icon } from 'react-fa'
import NavigationDrawer from './NavigationDrawer'
import DirectionsDrawer from './DirectionsDrawer'

class Navigation extends Component
{
    constructor(props)
    {
        super(props);
        this._event_onMenuClick = this._event_onMenuClick.bind(this);
        this._event_onDirectionsClick = this._event_onDirectionsClick.bind(this);
        this._render_borderRadius = this._render_borderRadius.bind(this);
        this._render_leftNavigation = this._render_leftNavigation.bind(this);
        this._render_leftBorder = this._render_leftBorder.bind(this);

        this.PlaceHolderRegular = "Search SBHS Maps";
        this.PlaceHolderDirections = "Choose Start..."

        this.DrawerIconClosed = "angle-down"; //bars | angle-down
        this.DrawerIconOpened = "angle-up";
        this.DirectionsIconClosed = "map-marker";
        this.DirectionsIconOpened = "angle-up";
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
        if (!this.state.Directions)
        {
            dirIcon = this.DirectionsIconOpened;
            pT = this.PlaceHolderDirections;
        }
        else
        {
            dirIcon = this.DirectionsIconClosed;     
            pT = this.PlaceHolderRegular;
        }
        this.setState({
            Drawer: false,
            DrawerIcon: this.DrawerIconClosed,
            Directions: !this.state.Directions,
            DirectionsIcon: dirIcon,
            PlaceHolder: pT
        });
    }
    componentWillMount()
    {
        this.setState({
            Drawer: false,
            DrawerIcon: this.DrawerIconClosed,
            Directions: false,
            DirectionsIcon: this.DirectionsIconClosed,
            borderRadius: 0,
            PlaceHolder: this.PlaceHolderRegular
        });
    }
    _render_borderRadius()
    {
        if (this.state.Drawer || this.state.Directions)
        {
            if (this.state.borderRadius < 10)
            {
                setTimeout(() =>
                {
                    this.setState({
                        borderRadius: this.state.borderRadius + 1
                    });
                }, 10);
            }
        }
        else
        {
            if (this.state.borderRadius > 0)
            {
                setTimeout(() =>
                {
                    this.setState({
                        borderRadius: this.state.borderRadius - 1
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
                    <Button outline color="primary" active={this.state.Drawer} onClick={this._event_onMenuClick} style={{ paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, border: "none", borderBottomLeftRadius: 0, borderTopLeftRadius: this.state.borderRadius }}>
                        <Icon name={this.state.DrawerIcon} size="lg" />
                    </Button>
                </InputGroupButton>
            );
        }
        else
        {
            return (
                <InputGroupAddon style={{ paddingLeft: 0, paddingRight: 0, width: 50, paddingTop: 12, paddingBottom: 12, border: "none", borderBottomLeftRadius: 0, borderTopLeftRadius: this.state.borderRadius }}>
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
                    borderTopLeftRadius: this.state.borderRadius,
                    borderTopRightRadius: this.state.borderRadius
                }}>
                    <InputGroup style={{ border: "none" }}>
                        {this._render_leftNavigation()}
                        {this._render_leftBorder()}
                        <Input style={{ border: "none" }} placeholder={this.state.PlaceHolder} />
                        <InputGroupButton>
                            <Button outline color="primary" style={{ padding: 0, width: 50, border: "none" }}>
                                <Icon name="search" />
                            </Button>
                        </InputGroupButton>
                        <InputGroupAddon style={{ padding: 0, width: 2, marginTop: 10, marginBottom: 10 }}></InputGroupAddon>
                        <InputGroupButton>
                            <Button outline color="success" active={this.state.Directions} onClick={this._event_onDirectionsClick} style={{ padding: 0, width: 50, border: "none", borderBottomRightRadius: 0, borderTopRightRadius: this.state.borderRadius }}>
                                <Icon name={this.state.DirectionsIcon} />
                            </Button>
                        </InputGroupButton>
                    </InputGroup>
                </div>
                <Collapse isOpen={this.state.Drawer} navbar>
                    <NavigationDrawer />
                </Collapse>
                <Collapse isOpen={this.state.Directions} navbar>
                    <DirectionsDrawer />
                </Collapse>
                {this._render_borderRadius()}
            </div>
        );
    }
}
export default Navigation;

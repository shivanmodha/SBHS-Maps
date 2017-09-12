import React, { Component } from 'react';
import { Button, Nav, NavItem, NavLink } from 'reactstrap';
import { Icon } from 'react-fa';
class NavigationDrawer extends Component
{
    render()
    {
        return (
            <div style={{
                backgroundColor: "#f7f7f9",
                padding: 10,
                marginTop: 5,
                borderBottomLeftRadius: 10,
                borderBottomRightRadius: 10,
                boxShadow: "1px 1px 5px rgba(0, 0, 0, 0.25)"
            }}>
                <Nav tabs>
                    <NavItem>
                        <NavLink active href="#">Home</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="#">Options</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="#">About</NavLink>
                    </NavItem>
                    <Button outline style={{ marginLeft: 66, borderBottom: "none", borderBottomLeftRadius: 0, borderBottomRightRadius: 0 }}>
                        <Icon name="download" />
                    </Button>
                </Nav>
                <div style={{ backgroundColor: "white" }}>

                </div>
            </div>
        );
    }
}
export default NavigationDrawer;

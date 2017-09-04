import React, { Component } from 'react';
import { Collapse, Navbar, NavbarToggler, NavbarBrand, Nav, NavItem, NavLink } from "reactstrap";

class App extends Component
{
    render()
    {
        return (
            <Navbar color="faded" light toggleable>
                <NavbarBrand>SBHS Maps</NavbarBrand>
                <Nav tabs navbar>
                    <NavItem>
                        <NavLink active href="#">Components</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="#">Components</NavLink>
                    </NavItem>
                </Nav>
                <Nav className="ml-auto" tabs navbar>
                    <NavItem>
                        <NavLink active href="#">Components</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="#">Components</NavLink>
                    </NavItem>
                </Nav>
            </Navbar>
        );
    }
}
export default App;

import React, { Component } from 'react';
import { Collapse, Navbar, NavbarToggler, NavbarBrand, Nav, NavItem, NavLink } from "reactstrap";

class App extends Component
{
    render()
    {
        return (
            <Navbar color="faded" light toggleable>
                <NavbarBrand href="/">reactstrap</NavbarBrand>
                <Nav className="ml-auto" navbar>
                    <NavItem>
                        <NavLink href="/components/">Components</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink href="https://github.com/reactstrap/reactstrap">Github</NavLink>
                    </NavItem>
                </Nav>
            </Navbar>
        );
    }
}
export default App;

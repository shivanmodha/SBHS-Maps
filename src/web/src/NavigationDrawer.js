import React, { Component } from 'react';
import { Button, ButtonGroup, Nav, NavItem, NavLink } from 'reactstrap';
import { Icon } from 'react-fa';
import HomePage from './HomePage.js';
import AboutPage from './AboutPage';
class NavigationDrawer extends Component
{
    componentWillMount()
    {
        this.setState({
            itms: [
                {
                    selected: true,
                    text: "Home",
                    return: () =>
                    {
                        return (<HomePage />);
                    }
                },
                {
                    selected: false,
                    text: "About",
                    return: () =>
                    {
                        return (<AboutPage />);
                    }
                }
            ]
        })
    }
    _render_tab(i)
    {
        let cH = () => {
            for (let j = 0; j < this.state.itms.length; j++)
            {
                if (i != j)
                {
                    this.state.itms[j].selected = false;
                }
                else
                {
                    this.state.itms[j].selected = true;
                }
            }
        }
        if (this.state.itms[i].selected)
        {
            return (
                <NavItem onClick={cH}>
                    <NavLink active href="#" style={{ backgroundColor: "rgba(247, 247, 249, 1)", borderBottom: "1px solid rgba(0, 0, 0, 0)" }}>{this.state.itms[i].text}</NavLink>
                </NavItem>
            );
        }
        else
        {
            return (
                <NavItem onClick={cH}>
                    <NavLink href="#">{this.state.itms[i].text}</NavLink>
                </NavItem>
            );
        }
    }
    _render_tabs()
    {
        let itms = [];
        for (let i = 0; i < this.state.itms.length; i++)
        {
            itms.push(this._render_tab(i));
        }
        return (
            <Nav tabs>
                {itms}
                <ButtonGroup style={{ marginLeft: 110 }}>
                    <Button outline style={{ borderBottom: "none", borderBottomLeftRadius: 0, borderBottomRightRadius: 0 }}>
                        <Icon name="download" />
                    </Button>
                    <Button outline color="success" style={{ borderBottom: "none", borderBottomLeftRadius: 0, borderBottomRightRadius: 0 }}>
                        <Icon name="github" />
                    </Button>
                </ButtonGroup>    
            </Nav>
        );
    }
    _render_content()
    {
        for (let i = 0; i < this.state.itms.length; i++)
        {
            if (this.state.itms[i].selected)
            {
                return this.state.itms[i].return();
            }
        }
        return null;
    }
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
                {this._render_tabs()}
                <div style={{ borderBottomLeftRadius: 10, borderBottomRightRadius: 10, borderLeft: "1px solid rgba(215, 215, 215, 1)", borderRight: "1px solid rgba(215, 215, 215, 1)", borderBottom: "1px solid rgba(215, 215, 215, 1)" }}>
                    {this._render_content()}
                </div>
            </div>
        );
    }
}
export default NavigationDrawer;

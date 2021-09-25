import './App.css';
import React from 'react';
import { withAuthenticator } from '@aws-amplify/ui-react';
import { MuiThemeProvider, createTheme } from '@material-ui/core/styles';
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";

import Home from './Home';
import Dashboard from './Dashboard';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import TaskList from './TaskList';
import TaskEdit from "./TaskEdit";
import CampaignList from './CampaignList';
import CampaignEdit from "./CampaignEdit";
import AnnotationHome from "./AnnotationHome";
import ImageViewer from "./ImageViewer";
import Profile from "./Profile";

const theme = createTheme({
    palette: {
        primary: {
            main: 'rgba(0, 0, 0, 0.87)'
        }
    }
});

class App extends React.Component {


  render() {
    return (
        <Router>
            <MuiThemeProvider theme={theme}>
              <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/dashboard' exact={true} component={Dashboard}/>

                <Route path='/annotation' exact={true} component={AnnotationHome}/>
                <Route path='/tasks' exact={true} component={TaskList}/>
                <Route path='/tasks/:id' component={TaskEdit}/>
                <Route path='/campaigns' exact={true} component={CampaignList}/>
                <Route path='/campaigns/:id' component={CampaignEdit}/>
                <Route path='/imageViewer' component={ImageViewer}/>
                <Route path='/imageViewer/:id' component={ImageViewer}/>
                <Route path='/benchmark' component={() => {
                    window.location.href = 'https://health.aiaudit.org/';
                    return null;
                }}/>
                  <Route
                      exact
                      path="/profile"
                      component={Profile}
                  />
              </Switch>
            </MuiThemeProvider>
        </Router>
    );
  }
}

export default withAuthenticator(App);

import React, { Component } from 'react';

// Import ant design components
import Button from 'antd/lib/button';
import message from 'antd/lib/message';
import Table from 'antd/lib/table';

// Import custom components
import LoginForm from '@/components/EmployeeLoginForm/EmployeeLoginForm';
import StarForm from '@/components/StarForm/StarForm';
import MovieForm from '@/components/MovieForm/MovieForm'

import styles from './styles';

// Import API
import API from '@/api/index';

class Dashboard extends Component {
  state = {
    isLogin: false,
    loginForm: {
      email: '',
      password: '',
    },
    starForm: {
      name: '',
      birthYear: '',
    },
    movieForm: {
      title: '',
      year: '',
      director: '',
      star: '',
      genre: '',
    },
    tables: [],
    isLoginFormValid: false,
    isStarFormValid: false,
  }

  // Listen to login form values
  onLoginValuesChange = (event, isError, values) => {
    this.setState({
      isLoginFormValid: !isError,
      loginForm: values
    })
  }

  // Listen to Star form values
  onStarValuesChange = (event, isError, values) => {
    this.setState({
      isStarFormValid: !isError,
      starForm: values
    })
  }

  // Listen to Star form values
  onMovieValuesChange = (event, isError, values) => {
    this.setState({
      isStarFormValid: !isError,
      movieForm: values
    })
  }

  getTable = () => {
    window.$axios(API.tables.GET())
      .then((response) => {
        let data = response.data

        if (data.type === 'success') {
          // Set local state
          this.setState({
            tables: data.message.tables
          });
        }
        else {
          message.error(`${data.message.errorMessage}`);
        }
      })
      .catch((error) => {
        console.log(error);
      })
  }
  
  // Send http request to login
  login = () => {
    // Check the login form validation
    if (this.state.isLoginFormValid) {
      window.$axios(API.employee.LOGIN({
        ...this.state.loginForm
      }))
        .then((response) => {
          let data = response.data

          if (data.type === 'success') {
            message.success('Welcome back');
            // Set local state
            this.setState({
              isLogin: true
            });

            this.getTable();
          }
          else {
            message.error(`${data.message.errorMessage}`);
          }
        })
        .catch((error) => {
          console.log(error);
        })
    }
    else {
      message.error('Please check your input!');
    }
  }

  insertStar = () => {
    // Check the login form validation
    if (this.state.isStarFormValid) {
      window.$axios(API.star.ADD_STAR({
        ...this.state.starForm
      }))
        .then((response) => {
          let data = response.data

          if (data.type === 'success') {
            message.success('Add the star successfully!');
          }
          else {
            message.error(`${data.message.errorMessage}`);
          }
        })
        .catch((error) => {
          console.log(error);
        })
    }
    else {
      message.error('Please check your input!');
    }
  }

  insertMovie = () => {
    // Check the login form validation
    if (this.state.isStarFormValid) {
      window.$axios(API.movie.ADD_MOVIE({
        ...this.state.movieForm
      }))
        .then((response) => {
          let data = response.data

          if (data.type === 'success') {
            message.success('Add the movie successfully!');
          }
          else {
            message.error(`${data.message.errorMessage}`);
          }
        })
        .catch((error) => {
          console.log(error);
        })
    }
    else {
      message.error('Please check your input!');
    }
  }

  render() {
    // State params
    const { isLogin, tables } = this.state;
    
    const columns = [{
      title: 'Attribute',
      dataIndex: 'name',
      key: 'name',
    }, {
      title: 'Type',
      dataIndex: 'type',
      key: 'type',
    }];

    return (
      <div>
        {
          !isLogin ?
          <div style={styles.container}>
            <img style={styles.logo} src={require('@/assets/img/utils/logo.png')} alt="logo"/>

            <LoginForm onLoginValuesChange={this.onLoginValuesChange}></LoginForm>

            <Button style={{width: '100%'}} type="primary" onClick={this.login}>Login</Button>
          </div>
          :
          <div>
            <div>
              <h2>Insert a star</h2>

              <StarForm onStarValuesChange={this.onStarValuesChange}></StarForm>

              <Button style={{width: '100%'}} type="primary" onClick={this.insertStar}>Insert this star</Button>
            </div>

            <hr style={styles.delimiter}/>

            <div>
              <h2>Insert a Movie</h2>

              <MovieForm onMovieValuesChange={this.onMovieValuesChange}></MovieForm>

              <Button style={{width: '100%'}} type="primary" onClick={this.insertMovie}>Insert this movie</Button>
            </div>

            <hr style={styles.delimiter}/>

            <div>
              {
                tables.map((table) => (
                  <div key={table.name}>
                    <h2>{table.name}</h2>
                    <Table style={{background: 'white'}} dataSource={table.attributes} columns={columns} pagination={false} />
                    <hr style={styles.delimiter}/>
                  </div>
                ))
              }
            </div>
          </div>
        }
      </div>
    );
  }
}

export default Dashboard;

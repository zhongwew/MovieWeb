import React, { Component } from 'react';

export default class Report extends Component {
  render() {
    return (
      <div>
        <h1 style={{textAlign: 'center'}}>Like predicate report</h1>
        <p>We mainly use like predicate in the search module.  When the result set is required to be partially equal to the keyword, we use like. 
          When the keyword is required to be completely equal to the result, like genres, we use '='.
        </p>
          <p>  For example: </p>
        <p>select * from movies where title like "TITLE%"</p>
        <p>select * from stars from stars where name like "%NAME%"</p>
        <p>And we take % as default value of parameters, so the search clause will be like: where title like "%", which will return all the titles.</p>
      </div>
    )
  }
}

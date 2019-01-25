import React, { Component } from 'react'
import {
  Link
} from 'react-router-dom';

import Mock from 'mockjs';

// Import store
import store from '@/store/index';
import searchActions from '@/store/modules/search/actions';
import scrollActions from '@/store/modules/scroll/actions';

// Import Ant Design components
import message from 'antd/lib/message';
import Dropdown from 'antd/lib/dropdown';
import Menu from 'antd/lib/menu';
import Icon from 'antd/lib/icon';
import Pagination from 'antd/lib/pagination';
import Button from 'antd/lib/button';
import AutoComplete from 'antd/lib/auto-complete'
import Input from 'antd/lib/input';

// Import Custom components
import Panel from '@/components/Tab/Panel/Panel';

// Import custom css styles
import './Tab.css';

import API from '@/api/index';

const Option = AutoComplete.Option;
const OptGroup = AutoComplete.OptGroup;

function renderTitle(title) {
  return (
    <span>
      {title}
    </span>
  );
}

export default class Tab extends Component {

  constructor(props) {
    super(props);

    this.state = {
      genres: [],
      titles: require(`./assets/titles.json`),
      list: [],
      isShowPanel: false,
      panelTitle: '',
      dataSource: [],
      keyword: '',
      isLoading: false,
      cacheDataSource: []
    }
  }

  componentDidMount() {
    window.$axios(API.genres.GET_GENRES())
      .then((response) => {
        let data = response.data;

        if (data.type === 'success') {
          this.setState({
            genres: data.message.genres
          })
        }
        else {
          message.error(data.message.errorMessage);
        }
      })
      .catch((error) => {
        message.error(`${error.request.status}: ${error.request.statusText}`);
      })
  }

  showPanel = (event, panelTitle) => {
    if (this.state.isShowPanel) {
      this.setState({
        isShowPanel: false
      })
    }
    else {
      this.setState({
        isShowPanel: true,
        panelTitle: panelTitle,
        list: panelTitle === 'Genre' ? this.state.genres : this.state.titles
      })
    }
  }

  hidePanel = () => {
    this.setState({
      isShowPanel: false
    })
  }

  // Change the number of count
  changeCount = async (count) => {
    const { updateCount, getMovieList } = this.props;

    await updateCount({
      count
    });

    getMovieList(store.getState().scroll.page, 0);
  }

  // Get sorted movie list
  sort = async (event, sortType) => {
    event.preventDefault();

    // Start searcing
    store.dispatch(searchActions.startSearch());
    await store.dispatch(scrollActions.updatePage({
      page: 1
    }));

    // Get sorted movie list
    window.$axios(API.movies.GET_MOVIES({
      page: store.getState().scroll.page,
      genre: store.getState().search.genre,
      title: store.getState().search.title,
      sortType: sortType,
      count: store.getState().scroll.count
    }))
      .then((response) => {
        let data = response.data;
        if (data.type === 'success') {
          // Get search results, and set it to store
          store.dispatch(searchActions.setResults({
            results: data.message.movies,
            genre: store.getState().search.genre,
            title: store.getState().search.title,
            sortType
          }))
        }
        else {
          message.error(data.message.errorMessage);
        }
      })
  }

  onSelect = (value) => {
    this.setState({
      keyword: value
    })
  }

  onChange = (value) => {
    this.setState({
      keyword: value
    })
  }

  handleSearch = (value) => {
    let { isLoading, cacheDataSource, keyword } = this.state;

    if (value.length < 4 || isLoading) return ;

    this.setState({
      isLoading: true
    })
    
    console.log(`Searching ${value}...`);
    console.log('Auto complete is initialized');

    // Load cache data source
    if (cacheDataSource[value]) {
      console.log('Loading cache data source');
      this.setState({
        dataSource: cacheDataSource[value],
      })
    }
    else {
      console.log('Send ajax to server');
      // Send ajax to server
      window.$axios(API.suggestion.GET_ITEMS({
        keyword: value
      }))
        .then((response) => {
          let data = response.data;
          if (data.type === 'success') {
            // Get search results, and set it to store
            cacheDataSource[value] = data.message.suggestions;
            this.setState({
              dataSource: data.message.suggestions,
              cacheDataSource: cacheDataSource
            })
          }
          else {
            message.error(data.message.errorMessage);
          }
        })
    }

    setTimeout(() => {
      this.setState({
        isLoading: false
      })
    }, 2000);
  }

  render() {
    const { isShowPanel, panelTitle, list, dataSource, keyword} = this.state;
    const { pageNum, page, getMovieList } = this.props;

    // Count
    const countMenu = (
      <Menu>
        <Menu.Item>
          <span onClick={() => this.changeCount(20)}>20 Items / Page</span>
        </Menu.Item>
        <Menu.Item>
          <span onClick={() => this.changeCount(50)}>50 Items / Page</span>
        </Menu.Item>
        <Menu.Item>
          <span onClick={() => this.changeCount(70)}>70 Items / Page</span>
        </Menu.Item>
      </Menu>
    );

    // Sort menu
    const sortMenu = (
      <Menu>
        <Menu.Item key="0">
          <a onClick={(event) => this.sort(event, 'TITLE_DESC')} style={{ padding: '10px 15px' }} >
            <Icon style={{ marginRight: 5 }} type="down" />
            <span>Title Desc</span>
          </a>
        </Menu.Item>
        <Menu.Item key="1">
          <a onClick={(event) => this.sort(event, 'TITLE_ASC')} href="" style={{ padding: '10px 15px' }}>
            <Icon style={{ marginRight: 5 }} type="up" />
            <span>Title Asc</span>
          </a>
        </Menu.Item>
        <Menu.Item key="2">
          <a onClick={(event) => this.sort(event, 'YEAR_DESC')} href="" style={{ padding: '10px 15px' }}>
            <Icon style={{ marginRight: 5 }} type="down" />
            <span>Year Desc</span>
          </a>
        </Menu.Item>
        <Menu.Item key="3">
          <a onClick={(event) => this.sort(event, 'YEAR_ASC')} href="" style={{ padding: '10px 15px' }}>
            <Icon style={{ marginRight: 5 }} type="up" />
            <span>Year Asc</span>
          </a>
        </Menu.Item>
      </Menu>
    )

    // Auto complete options
    const options = dataSource.map(group => (
      <OptGroup
        key={group.title}
        label={renderTitle(group.title)}
      >
        {group.children.map(opt => (
          <Option key={opt.id} value={opt.title}>
            {
              group.title === 'Movies' 
              ?
              <Link to={`/movie/${opt.id}/${Mock.Random.natural(1, 14)}`}>{opt.title}</Link>
              :
              <Link to={`/star/${opt.id}`}>{opt.title}</Link>
            }
          </Option>
        ))}
      </OptGroup>
    ));

    return (
      <div ref={(tabContainer) => this.tabContainer = tabContainer} className="tab-container">
        <div className="wrapper">
          <nav>
            <span onClick={(event) => this.showPanel(event, 'Genre')} className="nav-item">Genre</span>

            <span onClick={(event) => this.showPanel(event, 'Title')} className="nav-item">Title</span>

            <Dropdown overlay={sortMenu}>
              <span className="nav-item" style={{ marginLeft: 8 }}>
                Sort <Icon type="down" />
              </span>
            </Dropdown>

            {/* auto complete */}
            <AutoComplete
              className="certain-category-search"
              dropdownClassName="certain-category-search-dropdown"
              dropdownMatchSelectWidth={false}
              dataSource={options}
              placeholder="input here"
              optionLabelProp="value"
              onChange={this.onChange}
              onSelect={this.onSelect}
              onSearch={this.handleSearch}
            >
              <Input suffix={<Icon type="search" className="certain-category-icon" />} />
            </AutoComplete>

            <Link to={{
              pathname: '/search',
              search: `?keyword=${keyword}`
            }}>
              <Button type="primary" style={{marginLeft: 5}}>Search</Button>
            </Link>
          </nav>

          <div className="function-wrapper">
            <Dropdown overlay={countMenu}>
              <Button className="ant-dropdown-link" href="#">
                Count
              </Button>
            </Dropdown>

            <Pagination onChange={getMovieList} size="small" current={page} defaultPageSize={1} total={pageNum} />
          </div>
        </div>

        {/* nav panel */}
        <Panel isShowPanel={isShowPanel} title={panelTitle} list={list} genre={panelTitle}></Panel>
      </div>
    )
  }
}

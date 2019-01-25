import Mock from 'mockjs';

import { movie } from './modules/movie';
import { movies } from './modules/movies';
import { genres } from './modules/genres';
import { auth } from './modules/auth';
import { star } from './modules/star';
import { cart } from './modules/cart';
import { search } from './modules/search';
import { user } from './modules/user';
import { sales } from './modules/sales';
import { employee } from './modules/employee';
import { tables } from './modules/tables';
import { autoComplete } from './modules/autoComplete';
import { suggestion } from './modules/suggestion';

let data = [].concat(
  movie,
  movies,
  genres,
  auth,
  star,
  cart,
  search,
  user,
  sales,
  employee,
  tables,
  autoComplete,
  suggestion
)

data.forEach((res) => {
  Mock.mock(res.path, res.type, res.data);
});

export default Mock;